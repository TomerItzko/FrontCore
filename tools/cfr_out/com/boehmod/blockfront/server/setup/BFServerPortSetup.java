/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.server.setup;

import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.util.BFLog;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;

public class BFServerPortSetup {
    public static void patchServerPort(@NotNull BFServerManager manager) {
        if (!manager.isMatchMakingEnabled()) {
            return;
        }
        try (FileInputStream fileInputStream = new FileInputStream("server.properties");){
            int n = 25565;
            boolean bl = false;
            Properties properties = new Properties();
            properties.load(fileInputStream);
            BFLog.log("Original port is " + n, new Object[0]);
            while (!BFServerPortSetup.isPortAvailable(n)) {
                bl = true;
                ++n;
            }
            properties.setProperty("server-port", String.valueOf(n));
            if (bl) {
                BFLog.log("Server port is in use! Switched server port to '" + n + "'", new Object[0]);
            } else {
                BFLog.log("Server port '" + n + "' not in use! Ignoring.", new Object[0]);
            }
            fileInputStream.close();
            FileOutputStream fileOutputStream = new FileOutputStream("server.properties");
            properties.store(fileOutputStream, null);
            fileOutputStream.close();
            BFLog.log("Successfully checked and patched server port!", new Object[0]);
        }
        catch (IOException iOException) {
            BFLog.logThrowable("Error while checking server port.", iOException, new Object[0]);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isPortAvailable(int port) {
        ServerSocket serverSocket = null;
        DatagramSocket datagramSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            datagramSocket = new DatagramSocket(port);
            datagramSocket.setReuseAddress(true);
            boolean bl = true;
            return bl;
        }
        catch (IOException iOException) {
        }
        finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                }
                catch (IOException iOException) {}
            }
        }
        return false;
    }
}

