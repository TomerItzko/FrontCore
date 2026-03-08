/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.cloud.connection;

import com.boehmod.bflib.cloud.common.ConnectionType;
import com.boehmod.bflib.cloud.connection.Connection;
import com.boehmod.bflib.cloud.connection.ConnectionStatus;
import com.boehmod.bflib.cloud.connection.ConnectionStatusContext;
import com.boehmod.bflib.cloud.connection.EncryptedConnectionCredentials;
import com.boehmod.bflib.cloud.encryption.AESDecryptionHandler;
import com.boehmod.bflib.cloud.encryption.AESEncryptionHandler;
import com.boehmod.bflib.cloud.encryption.EncryptionUtils;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.PacketRegistry;
import com.boehmod.bflib.cloud.packet.primitives.ClientHeartBeatPacket;
import com.boehmod.bflib.cloud.packet.primitives.ClientLoginPacket;
import com.boehmod.bflib.cloud.packet.primitives.ClientLogoutPacket;
import com.boehmod.bflib.cloud.packet.primitives.EncryptionKeyExchangePacket;
import com.boehmod.bflib.cloud.packet.primitives.EncryptionReadyPacket;
import com.boehmod.blockfront.cloud.connection.BFConnectionChannelInitializer;
import com.boehmod.blockfront.cloud.connection.IConnectionDetails;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.util.BFLog;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.crypto.SecretKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BFConnection
extends Connection<PlayerCloudData> {
    @NotNull
    private final ExecutorService connectionExecutor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "Cloud-Connection-Worker");
        thread.setDaemon(true);
        return thread;
    });
    public static final int CONNECT_TIME_MAX_TICKS = 600;
    public static final int LOGIN_TIME_MAX_TICKS = 100;
    @NotNull
    private static final AtomicBoolean ATTEMPTING_CONNECTION = new AtomicBoolean(false);
    public static final int MAX_IDLE_TICKS = 600;
    public static final int HEARTBEAT_INTERVAL_TICKS = 300;
    @NotNull
    private final IConnectionDetails clientConnection;
    @NotNull
    private final BFAbstractManager<?, ?, ?> modManager;
    @NotNull
    private final EventLoopGroup eventLoopGroup;
    @Nullable
    private Channel channel;
    @NotNull
    private final Queue<IPacket> incomingPackets = new ConcurrentLinkedQueue<IPacket>();
    private volatile boolean running = false;
    private int connectionHeartBeat = 0;
    private int connectionTick = 600;
    private int loginSendTimer = 100;
    @Nullable
    private PlayerCloudData playerCloudData = null;
    @Nullable
    private KeyPair clientKeyPair;

    public BFConnection(@NotNull IConnectionDetails details, @NotNull BFAbstractManager<?, ?, ?> manager) {
        super(600);
        this.clientConnection = details;
        this.modManager = manager;
        this.eventLoopGroup = new NioEventLoopGroup(2);
    }

    public int getLoginSendTimer() {
        return this.loginSendTimer;
    }

    public void attemptConnection() {
        if (!this.getStatus().isClosed()) {
            return;
        }
        try {
            String string = this.clientConnection.getLocation();
            int n = this.clientConnection.getCloudPort();
            BFLog.log("[Connection Handler] Connecting to '%s:%d'", string, n);
            Bootstrap bootstrap = new Bootstrap();
            ((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)bootstrap.group(this.eventLoopGroup)).channel(NioSocketChannel.class)).option(ChannelOption.TCP_NODELAY, (Object)true)).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (Object)30000)).handler((ChannelHandler)new BFConnectionChannelInitializer(this));
            ChannelFuture channelFuture2 = bootstrap.connect(string, n);
            channelFuture2.addListener((GenericFutureListener)((ChannelFutureListener)channelFuture -> {
                try {
                    if (channelFuture.isSuccess()) {
                        BFLog.log("[Connection Handler] Connection established", new Object[0]);
                        this.channel = channelFuture.channel();
                        this.running = true;
                        this.sendCredentials();
                        BFLog.log("[Cloud] Established connection to the cloud.", new Object[0]);
                    } else {
                        this.setStatus(ConnectionStatus.CLOSED, ConnectionStatusContext.CLIENT);
                        BFLog.log("[Connection Handler] Failed to connect to the cloud: %s", channelFuture.cause().getMessage());
                        if (!channelFuture.isCancelled()) {
                            channelFuture.cancel(true);
                        }
                    }
                    ATTEMPTING_CONNECTION.set(false);
                }
                catch (Exception exception) {
                    try {
                        BFLog.logThrowable("[Connection Handler] Error in connection callback", exception, new Object[0]);
                        this.setStatus(ConnectionStatus.CLOSED, ConnectionStatusContext.CLIENT);
                        ATTEMPTING_CONNECTION.set(false);
                    }
                    catch (Throwable throwable) {
                        ATTEMPTING_CONNECTION.set(false);
                        throw throwable;
                    }
                }
            }));
        }
        catch (Exception exception) {
            BFLog.logThrowable("[Connection Handler] An error occurred while attempting to connect to the cloud.", exception, new Object[0]);
            this.setStatus(ConnectionStatus.CLOSED, ConnectionStatusContext.CLIENT);
            ATTEMPTING_CONNECTION.set(false);
        }
    }

    private void sendCredentials() {
        if (this.channel == null || !this.channel.isActive()) {
            return;
        }
        try {
            PublicKey publicKey = null;
            try {
                this.clientKeyPair = EncryptionUtils.generateECDHKeyPair();
                publicKey = this.clientKeyPair.getPublic();
                BFLog.log("[Connection Handler] Generated ECDH key pair for encryption", new Object[0]);
            }
            catch (Exception exception) {
                BFLog.log("[Connection Handler] Failed to generate encryption keys, proceeding without encryption: %s", exception.getMessage());
            }
            EncryptedConnectionCredentials encryptedConnectionCredentials = new EncryptedConnectionCredentials(this.clientConnection.getType(), this.getUUID(), this.getUsername(), this.getVersion(), this.getVersionHash(), this.getHardwareId(), publicKey);
            BFLog.log("[Connection Handler] Sending credentials - UUID: %s, Username: %s, Channel: %s, Encryption: %s", this.getUUID(), this.getUsername(), this.clientConnection.getType(), publicKey != null ? "ENABLED" : "DISABLED");
            ByteBuf byteBuf = Unpooled.buffer();
            encryptedConnectionCredentials.writeCredentials(byteBuf);
            this.channel.writeAndFlush((Object)byteBuf);
        }
        catch (Exception exception) {
            BFLog.logThrowable("[Connection Handler] Error sending credentials", exception, new Object[0]);
            this.disconnect("Error sending credentials: " + exception.getMessage(), false);
        }
    }

    protected void addIncomingPacket(@NotNull IPacket packet) {
        this.incomingPackets.add(packet);
    }

    public void handleEncryptionKeyExchange(@NotNull EncryptionKeyExchangePacket encryptionKeyExchangePacket) {
        if (this.clientKeyPair == null) {
            BFLog.log("[Connection Handler] Received encryption key but client key pair is null", new Object[0]);
            return;
        }
        try {
            PublicKey publicKey = encryptionKeyExchangePacket.serverPublicKey();
            SecretKey secretKey = EncryptionUtils.deriveSharedSecret((PrivateKey)this.clientKeyPair.getPrivate(), (PublicKey)publicKey);
            if (this.channel != null && this.channel.pipeline() != null) {
                ChannelPipeline channelPipeline = this.channel.pipeline();
                AESDecryptionHandler aESDecryptionHandler = new AESDecryptionHandler(secretKey);
                AESEncryptionHandler aESEncryptionHandler = new AESEncryptionHandler(secretKey, false);
                channelPipeline.addAfter("frameEncoder", "encryption", (ChannelHandler)aESEncryptionHandler);
                channelPipeline.addAfter("frameDecoder", "decryption", (ChannelHandler)aESDecryptionHandler);
                aESDecryptionHandler.activateDecryption();
                aESEncryptionHandler.activateEncryption();
                BFLog.log("[Connection Handler] Encryption activated successfully", new Object[0]);
                this.sendPacket((IPacket)new EncryptionReadyPacket());
                BFLog.log("[Connection Handler] Sent encryption ready confirmation to server", new Object[0]);
            }
        }
        catch (Exception exception) {
            BFLog.logThrowable("[Connection Handler] Failed to establish encryption", exception, new Object[0]);
            this.disconnect("Failed to establish encryption: " + exception.getMessage(), false);
        }
    }

    private void sendLogin() {
        if (!this.running || this.channel == null || !this.channel.isActive()) {
            return;
        }
        try {
            ClientLoginPacket clientLoginPacket = new ClientLoginPacket(this.clientConnection.getServerId(), this.clientConnection.getMinecraftPort());
            this.sendPacket((IPacket)clientLoginPacket);
            BFLog.log("[Connection Handler] Login packet sent", new Object[0]);
        }
        catch (Exception exception) {
            BFLog.logThrowable("[Connection Handler] Error sending login packet", exception, new Object[0]);
            this.disconnect("Error sending login packet: " + exception.getMessage(), false);
        }
    }

    public int getReconnectTickCount() {
        return 600 - this.connectionTick;
    }

    public void sendPacket(@NotNull IPacket packet) {
        if (!this.running || this.channel == null || !this.channel.isActive()) {
            return;
        }
        try {
            this.channel.writeAndFlush((Object)packet);
        }
        catch (Exception exception) {
            BFLog.logThrowable("[Connection Handler] Failed to send packet", exception, new Object[0]);
            this.disconnect("Failed to send packet: " + exception.getMessage(), false);
        }
    }

    public <T extends IPacket> void onIllegalPacket(@NotNull T packet, @NotNull ConnectionType typeActual, @NotNull ConnectionType typeExpected) {
        BFLog.log("[Connection Handler] Illegal packet: %s (Expected: %s, Actual: %s)", packet.getClass().getSimpleName(), typeExpected, typeActual);
    }

    public void onConnectionStatusChanged(@NotNull ConnectionStatus connectionStatus, @NotNull ConnectionStatusContext connectionStatusContext) {
        if (connectionStatusContext == ConnectionStatusContext.CLOUD) {
            BFLog.log("[Connection Handler] Received status change from cloud to '%s'.", connectionStatus.name());
        } else {
            BFLog.log("[Connection Handler] Performed local status change to '%s'.", connectionStatus.name());
        }
        if (connectionStatus != ConnectionStatus.CONNECTED_VERIFIED) {
            this.loginSendTimer = 100;
        }
    }

    public PlayerCloudData getPlayerCloudData() {
        if (this.getType() != ConnectionType.PLAYER) {
            throw new IllegalStateException("Cannot get player data for non-player connection.");
        }
        if (this.playerCloudData != null) {
            return this.playerCloudData;
        }
        this.playerCloudData = ((PlayerDataHandler)this.modManager.getPlayerDataHandler()).getCloudProfile(this.getUUID());
        return this.playerCloudData;
    }

    public void setStatus(@NotNull ConnectionStatus connectionStatus, @NotNull ConnectionStatusContext connectionStatusContext) {
        ConnectionStatus connectionStatus2 = this.getStatus();
        super.setStatus(connectionStatus, connectionStatusContext);
        if (connectionStatus2 != connectionStatus) {
            this.modManager.connectionStatusChanged(connectionStatus2, connectionStatus, connectionStatusContext);
        }
    }

    protected void onUpdate() {
        IPacket iPacket;
        int n = 0;
        while ((iPacket = this.incomingPackets.poll()) != null && n < 10) {
            try {
                if (this.shouldHandlePacket(iPacket)) {
                    PacketRegistry.processPacket((IPacket)iPacket, (ConnectionType)this.getType(), (Connection)this);
                }
                ++n;
            }
            catch (Exception exception) {
                BFLog.logThrowable("[Connection Handler] Error processing packet", exception, new Object[0]);
            }
        }
        if (this.getStatus().isClosed()) {
            if (this.connectionTick++ > 600) {
                this.connectionTick = 0;
                if (ATTEMPTING_CONNECTION.compareAndSet(false, true)) {
                    this.connectionExecutor.submit(() -> {
                        try {
                            if (this.clientConnection.updateMatchMaking()) {
                                this.attemptConnection();
                            }
                            ATTEMPTING_CONNECTION.set(false);
                        }
                        catch (Exception exception) {
                            try {
                                BFLog.logThrowable("[Connection Handler] Error in connection check thread", exception, new Object[0]);
                                ATTEMPTING_CONNECTION.set(false);
                            }
                            catch (Throwable throwable) {
                                ATTEMPTING_CONNECTION.set(false);
                                throw throwable;
                            }
                        }
                    });
                }
            }
        } else {
            if (this.getStatus() == ConnectionStatus.CONNECTED_NOT_VERIFIED) {
                if (this.loginSendTimer-- == 0) {
                    BFLog.log("[Connection Handler] Sending login attempt to cloud...", new Object[0]);
                    this.sendLogin();
                }
            } else {
                this.loginSendTimer = 100;
            }
            if (this.connectionHeartBeat++ >= 300) {
                this.sendPacket((IPacket)new ClientHeartBeatPacket());
                this.connectionHeartBeat = 0;
            }
        }
    }

    public void bumpReconnectTick() {
        this.connectionTick = 600;
    }

    protected boolean shouldHandlePacket(@NotNull IPacket packet) {
        return true;
    }

    public void disconnect(@NotNull String string, boolean bl) {
        if (this.isConnectionClosed()) {
            return;
        }
        if (bl && this.running && this.channel != null && this.channel.isActive()) {
            try {
                ClientLogoutPacket clientLogoutPacket = new ClientLogoutPacket();
                ByteBuf byteBuf = Unpooled.buffer();
                PacketRegistry.writePacket((IPacket)clientLogoutPacket, (ByteBuf)byteBuf);
                int n = byteBuf.readableBytes();
                ByteBuf byteBuf2 = Unpooled.buffer((int)(4 + n));
                byteBuf2.writeInt(n);
                byteBuf2.writeBytes(byteBuf);
                this.channel.writeAndFlush((Object)byteBuf2).syncUninterruptibly();
                byteBuf.release();
            }
            catch (Exception exception) {
                BFLog.logThrowable("[Connection Handler] Error sending logout packet", exception, new Object[0]);
            }
        }
        if (this.channel != null) {
            this.channel.close();
            this.channel = null;
        }
        this.running = false;
        this.setStatus(ConnectionStatus.CLOSED, ConnectionStatusContext.CLIENT);
        BFLog.log("[Connection Handler] Disconnected: %s", string);
        BFLog.log("[Cloud] Lost connection to the cloud.", new Object[0]);
        this.loginSendTimer = 100;
    }

    public void shutdown() {
        this.disconnect("Client shutting down", true);
        try {
            this.connectionExecutor.shutdown();
            if (!this.connectionExecutor.awaitTermination(500L, TimeUnit.MILLISECONDS)) {
                this.connectionExecutor.shutdownNow();
            }
        }
        catch (InterruptedException interruptedException) {
            this.connectionExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        this.eventLoopGroup.shutdownGracefully();
    }

    @NotNull
    public UUID getUUID() {
        return this.clientConnection.getUUID();
    }

    public byte[] getHardwareId() {
        return this.clientConnection.getHardwareId();
    }

    @NotNull
    public String getUsername() {
        return this.clientConnection.getUsername();
    }

    @NotNull
    public String getVersion() {
        return this.clientConnection.getVersion();
    }

    @NotNull
    public String getVersionHash() {
        return this.clientConnection.getVersionHash();
    }

    @NotNull
    public ConnectionType getType() {
        return this.clientConnection.getType();
    }
}

