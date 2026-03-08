/*
 * Decompiled with CFR.
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
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @NotNull
    public static String hashMD5(@NotNull Path path) throws NoSuchAlgorithmException, IOException {
        Object object;
        Object object2;
        MessageDigest messageDigest;
        block8: {
            messageDigest = MessageDigest.getInstance("MD5");
            try {
                object2 = Files.newInputStream(path, new OpenOption[0]);
                try {
                    int n;
                    object = new byte[8192];
                    while ((n = ((InputStream)object2).read((byte[])object)) != -1) {
                        messageDigest.update((byte[])object, 0, n);
                    }
                    if (object2 == null) break block8;
                }
                catch (Throwable throwable) {
                    if (object2 == null) throw throwable;
                    try {
                        ((InputStream)object2).close();
                        throw throwable;
                    }
                    catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                    throw throwable;
                }
                ((InputStream)object2).close();
            }
            catch (IOException iOException) {
                BFLog.logError("Failed to calculate MD5 hash for file: " + String.valueOf(path), new Object[0]);
            }
        }
        object2 = messageDigest.digest();
        object = new StringBuilder();
        Object object3 = object2;
        int n = ((Object)object3).length;
        int n2 = 0;
        while (n2 < n) {
            Object object4 = object3[n2];
            ((StringBuilder)object).append(String.format("%02x", (byte)object4));
            ++n2;
        }
        return ((StringBuilder)object).toString();
    }
}

