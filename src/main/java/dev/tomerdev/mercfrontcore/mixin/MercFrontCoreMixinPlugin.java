package dev.tomerdev.mercfrontcore.mixin;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class MercFrontCoreMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.endsWith(".client.TitleScreenMixin")) {
            return hasAll(
                "com.boehmod.blockfront.BlockFront",
                "com.boehmod.blockfront.client.screen.title.LobbyTitleScreen"
            );
        }
        if (mixinClassName.endsWith(".client.BFIntroScreenMixin")) {
            return hasAll(
                "com.boehmod.blockfront.client.screen.intro.BFIntroScreen"
            );
        }
        if (mixinClassName.endsWith(".client.BFClientScreenSubscriberMixin")) {
            return hasAll(
                "com.boehmod.blockfront.client.event.BFClientScreenSubscriber"
            );
        }
        if (mixinClassName.endsWith(".client.BFPlayerNetworkSubscriberMixin")) {
            return hasAll(
                "com.boehmod.blockfront.client.event.BFPlayerNetworkSubscriber"
            );
        }
        if (mixinClassName.endsWith(".client.TitleSidebarScreenMixin")) {
            return hasAll(
                "com.boehmod.blockfront.client.gui.widget.BFButton",
                "com.boehmod.blockfront.client.screen.title.sidebar.TitleSidebarScreen",
                "com.boehmod.blockfront.client.screen.SidebarScreen"
            );
        }
        if (mixinClassName.endsWith(".client.MatchPauseScreenMixin")) {
            return hasAll(
                "com.boehmod.blockfront.client.gui.widget.BFButton",
                "com.boehmod.blockfront.client.screen.match.MatchPauseScreen",
                "com.boehmod.blockfront.client.screen.BFMenuScreen"
            );
        }
        if (mixinClassName.endsWith(".BFServerManagerMixin")) {
            return hasAll(
                "com.boehmod.blockfront.server.BFServerManager"
            );
        }
        if (mixinClassName.endsWith(".InfectedGameVendorRelocateMixin")) {
            return hasAll(
                "com.boehmod.blockfront.game.impl.inf.InfectedGame"
            );
        }
        if (mixinClassName.endsWith(".BFCommonCloudPacketHandlersMixin")) {
            return hasAll(
                "com.boehmod.blockfront.cloud.common.BFCommonCloudPacketHandlers"
            );
        }
        if (mixinClassName.endsWith(".AbstractGameWinnerSkinDropMixin")) {
            return hasAll(
                "com.boehmod.blockfront.game.AbstractGame"
            );
        }
        if (mixinClassName.endsWith(".AbstractGameClassChangeGuardMixin")) {
            return hasAll(
                "com.boehmod.blockfront.game.AbstractGame"
            );
        }
        if (mixinClassName.endsWith(".AbstractGamePlayerManagerLoadoutXpMixin")) {
            return hasAll(
                "com.boehmod.blockfront.game.AbstractGamePlayerManager"
            );
        }
        if (mixinClassName.endsWith(".AbstractGamePlayerManagerAccessor")) {
            return hasAll(
                "com.boehmod.blockfront.game.AbstractGamePlayerManager"
            );
        }
        if (mixinClassName.endsWith(".BFGameChangeClassRequestPacketMixin")) {
            return hasAll(
                "com.boehmod.blockfront.common.net.packet.BFGameChangeClassRequestPacket"
            );
        }
        if (mixinClassName.endsWith(".BFUtilsLoadoutSkinMixin")) {
            return hasAll(
                "com.boehmod.blockfront.util.BFUtils"
            );
        }
        if (mixinClassName.endsWith(".client.AbstractMatchSelectClassScreenLoadoutXpMixin")) {
            return hasAll(
                "com.boehmod.blockfront.client.screen.match.AbstractMatchSelectClassScreen"
            );
        }
        if (mixinClassName.endsWith(".client.BF197LoadoutXpMixin")) {
            return hasAll(
                "com.boehmod.blockfront.unnamed.BF_197"
            );
        }
        if (mixinClassName.endsWith(".client.MatchSelectClassScreenLoadoutLockMixin")) {
            return hasAll(
                "com.boehmod.blockfront.client.screen.match.MatchSelectClassScreen"
            );
        }
        if (mixinClassName.endsWith(".PacketListenerPlayerActionMixin")) {
            return hasAll(
                "com.boehmod.blockfront.server.net.PacketListenerPlayerAction",
                "com.boehmod.blockfront.util.NettyUtils"
            );
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    private static boolean hasAll(String... classNames) {
        for (String className : classNames) {
            if (!hasClass(className)) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasClass(String className) {
        try {
            Class.forName(className, false, MercFrontCoreMixinPlugin.class.getClassLoader());
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
