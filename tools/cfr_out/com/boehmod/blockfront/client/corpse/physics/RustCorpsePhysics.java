/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Quaterniondc
 *  org.joml.Vector3dc
 */
package com.boehmod.blockfront.client.corpse.physics;

import com.boehmod.blockfront.client.corpse.physics.CorpsePhysicsBody;
import com.boehmod.blockfront.client.corpse.physics.a;
import com.boehmod.blockfront.unnamed.BF_14;
import com.boehmod.blockfront.util.BFLog;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;

public class RustCorpsePhysics {
    private static final String NAME = "corpse_physics";
    private static final String TEMP_NAME = "corpse_physics_natives";
    private static final boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
    private static final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
    public static boolean nativesLoaded = false;

    private static String getNativeFilename() {
        String string = System.getProperty("os.arch").equals("arm") || System.getProperty("os.arch").startsWith("aarch64") ? "aarch64" : "x86_64";
        if (isWindows) {
            return "corpse_physics_" + string + "_windows.dll";
        }
        if (isMac) {
            return "corpse_physics_" + string + "_macos.dylib";
        }
        return "corpse_physics_" + string + "_linux.so";
    }

    private static void loadNative() {
        String string = RustCorpsePhysics.getNativeFilename();
        try (InputStream inputStream = RustCorpsePhysics.class.getResourceAsStream("/natives/" + string);){
            if (inputStream == null) {
                throw new FileNotFoundException(NAME);
            }
            Path path = Files.createTempFile(TEMP_NAME, null, new FileAttribute[0]);
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            System.load(path.toAbsolutePath().toString());
            nativesLoaded = true;
        }
        catch (Exception exception) {
            nativesLoaded = false;
            BFLog.logThrowable("Failed to load `%s` native library", exception, string);
        }
    }

    public static native void initialize(double var0, double var2, double var4);

    public static native void tick(double var0);

    protected static native void createObject(int var0, double[] var1, double var2, double var4, double var6, double var8);

    protected static native void removeObject(int var0);

    protected static native double[] getPose(int var0);

    protected static native void addLinearAngularVelocities(int var0, double var1, double var3, double var5, double var7, double var9, double var11);

    protected static native void multiplyVelocities(int var0, double var1);

    protected static native void addRagdollConstraint(int var0, int var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22, double var24, double var26, double var28, double var30, double var32, double var34, double var36);

    public static native void addChunk(int var0, int var1, int var2, int[] var3);

    public static native void removeChunk(int var0, int var1, int var2);

    public static native void changeBlock(int var0, int var1, int var2, int var3);

    protected static native int uploadBlock();

    protected static native void addBox(int var0, double[] var1);

    public static native void clearLevel();

    @NotNull
    public static CorpsePhysicsBody method_779(int n, BF_14 bF_14, Vector3dc vector3dc, double d) {
        Vector3dc vector3dc2 = bF_14.getPosition();
        Quaterniondc quaterniondc = bF_14.method_109();
        RustCorpsePhysics.createObject(n, new double[]{vector3dc2.x(), vector3dc2.y(), vector3dc2.z(), quaterniondc.x(), quaterniondc.y(), quaterniondc.z(), quaterniondc.w()}, vector3dc.x(), vector3dc.y(), vector3dc.z(), d);
        return new CorpsePhysicsBody(n);
    }

    public static a method_778() {
        return new a(RustCorpsePhysics.uploadBlock());
    }

    static {
        RustCorpsePhysics.loadNative();
    }
}

