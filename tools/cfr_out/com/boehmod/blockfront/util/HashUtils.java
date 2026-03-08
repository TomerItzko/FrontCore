/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import com.boehmod.blockfront.util.BFLog;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.jetbrains.annotations.NotNull;

public class HashUtils {
    @NotNull
    public static String hashMD5(@NotNull Path path) throws NoSuchAlgorithmException, IOException {
        Object object;
        Object object2;
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        try {
            object2 = Files.newInputStream(path, new OpenOption[0]);
            try {
                int n;
                object = new byte[8192];
                while ((n = ((InputStream)object2).read((byte[])object)) != -1) {
                    messageDigest.update((byte[])object, 0, n);
                }
            }
            finally {
                if (object2 != null) {
                    ((InputStream)object2).close();
                }
            }
        }
        catch (IOException iOException) {
            BFLog.logError("Failed to calculate MD5 hash for file: " + String.valueOf(path), new Object[0]);
        }
        object2 = messageDigest.digest();
        object = new StringBuilder();
        for (Object object3 : object2) {
            ((StringBuilder)object).append(String.format("%02x", (byte)object3));
        }
        return ((StringBuilder)object).toString();
    }
}

