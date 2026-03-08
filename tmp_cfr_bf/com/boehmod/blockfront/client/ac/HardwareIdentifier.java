/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.ac;

import com.boehmod.blockfront.util.BFLog;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

public class HardwareIdentifier {
    private static final byte @NotNull [] EMPTY = new byte[0];
    private static final byte @NotNull [] INSTANCE_ID = HardwareIdentifier.generateHardwareID();

    public static byte @NotNull [] get() {
        return INSTANCE_ID;
    }

    private static byte @NotNull [] generateHardwareID() {
        BFLog.log("Generating enhanced unique and secure hardware ID.", new Object[0]);
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();
        CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
        List list = hardwareAbstractionLayer.getDiskStores();
        List list2 = hardwareAbstractionLayer.getNetworkIFs();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HardwareIdentifier.deNullifyString(HardwareIdentifier.sha512Hash(computerSystem.getHardwareUUID())));
        stringBuilder.append(HardwareIdentifier.deNullifyString(HardwareIdentifier.sha512Hash(computerSystem.getBaseboard().getSerialNumber())));
        stringBuilder.append(HardwareIdentifier.deNullifyString(HardwareIdentifier.sha512Hash(centralProcessor.getProcessorIdentifier().getProcessorID())));
        list.stream().map(HWDiskStore::getSerial).filter(string -> string != null && !string.isEmpty()).sorted().forEach(string -> stringBuilder.append(HardwareIdentifier.deNullifyString(HardwareIdentifier.sha512Hash(string))));
        list2.stream().map(NetworkIF::getMacaddr).filter(string -> string != null && !string.isEmpty()).sorted().forEach(string -> stringBuilder.append(HardwareIdentifier.deNullifyString(HardwareIdentifier.sha512Hash(string))));
        stringBuilder.append(HardwareIdentifier.deNullifyString(System.getProperty("os.name")));
        stringBuilder.append(HardwareIdentifier.deNullifyString(System.getProperty("os.arch")));
        stringBuilder.append(HardwareIdentifier.deNullifyString(System.getProperty("os.version")));
        String string2 = stringBuilder.toString();
        byte[] byArray = string2.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA3-512");
            return messageDigest.digest(byArray);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            BFLog.logThrowable("Failed to generate unique and secure hardware ID.", noSuchAlgorithmException, new Object[0]);
            return EMPTY;
        }
    }

    @NotNull
    private static String deNullifyString(@Nullable String string) {
        return string != null ? string : "";
    }

    @NotNull
    private static String sha512Hash(@Nullable String string) {
        if (string == null || string.isEmpty()) {
            return "";
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA3-512");
            byte[] byArray = messageDigest.digest(string.getBytes(StandardCharsets.UTF_8));
            return HardwareIdentifier.bytesToHexString(byArray);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return "";
        }
    }

    @NotNull
    private static String bytesToHexString(byte @NotNull [] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte by : bytes) {
            String string = Integer.toHexString(0xFF & by);
            if (string.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }
}

