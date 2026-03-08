/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.ac.check;

import com.boehmod.bflib.common.PatternReferences;
import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.ac.check.IAntiCheatCheck;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ResourcePacksCheck
implements IAntiCheatCheck<BFClientAntiCheat> {
    private static final String field_6276 = "assets/minecraft/shaders";
    private static final String field_6277 = "assets/bf/shaders";

    public static boolean checkResourcePacks(@NotNull Path path) throws IOException {
        boolean bl;
        if (!Files.isDirectory(path, new LinkOption[0])) {
            throw new IllegalArgumentException(String.valueOf(path) + " is not a directory.");
        }
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
        try {
            for (Path path2 : directoryStream) {
                String string;
                if (!Files.isRegularFile(path2, new LinkOption[0]) || !PatternReferences.XRAY.matcher(string = path2.getFileName().toString().toLowerCase(Locale.ROOT)).find()) break block9;
                bl = true;
                if (directoryStream == null) break block10;
            }
        }
        catch (Throwable throwable) {
            if (directoryStream != null) {
                try {
                    directoryStream.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
            }
            throw throwable;
        }
        {
            block9: {
                block10: {
                    directoryStream.close();
                }
                return bl;
            }
            continue;
        }
        if (directoryStream != null) {
            directoryStream.close();
        }
        return false;
    }

    public static boolean checkShaders(@NotNull Path path, final String string) throws IOException {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        Files.walkFileTree(path, (FileVisitor<? super Path>)new SimpleFileVisitor<Path>((List)objectArrayList){
            final /* synthetic */ List field_6278;
            {
                this.field_6278 = list;
            }

            @Override
            @NotNull
            public FileVisitResult visitFile(Path path, @NotNull BasicFileAttributes basicFileAttributes) throws IOException {
                if (path.toString().endsWith(".zip") && ResourcePacksCheck.method_5465(path.toFile(), string)) {
                    this.field_6278.add(path.toString());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            @NotNull
            public FileVisitResult preVisitDirectory(Path path, @NotNull BasicFileAttributes basicFileAttributes) {
                if (ResourcePacksCheck.method_5461(path.toFile(), string)) {
                    this.field_6278.add(path.toString());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            @NotNull
            public /* synthetic */ FileVisitResult visitFile(Object object, @NotNull BasicFileAttributes basicFileAttributes) throws IOException {
                return this.visitFile((Path)object, basicFileAttributes);
            }

            @Override
            @NotNull
            public /* synthetic */ FileVisitResult preVisitDirectory(Object object, @NotNull BasicFileAttributes basicFileAttributes) throws IOException {
                return this.preVisitDirectory((Path)object, basicFileAttributes);
            }
        });
        return !objectArrayList.isEmpty();
    }

    public static boolean method_5461(@NotNull File file, @NotNull String string) {
        File file2 = new File(file, string);
        return file2.exists() && file2.isDirectory();
    }

    public static boolean method_5465(@NotNull File file, @NotNull String string) throws IOException {
        boolean bl;
        ZipFile zipFile = new ZipFile(file);
        try {
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                if (!zipEntry.getName().startsWith(string) || zipEntry.isDirectory()) break block6;
                bl = true;
            }
        }
        catch (Throwable throwable) {
            try {
                zipFile.close();
            }
            catch (Throwable throwable2) {
                throwable.addSuppressed(throwable2);
            }
            throw throwable;
        }
        {
            block6: {
                zipFile.close();
                return bl;
            }
            continue;
        }
        zipFile.close();
        return false;
    }

    @Override
    public boolean run(@NotNull Minecraft minecraft, @NotNull BFClientAntiCheat bFClientAntiCheat) {
        try {
            Path path = minecraft.getResourcePackDirectory();
            boolean bl = ResourcePacksCheck.checkResourcePacks(path);
            boolean bl2 = ResourcePacksCheck.checkShaders(path, field_6276);
            boolean bl3 = ResourcePacksCheck.checkShaders(path, field_6277);
            return bl || bl2 || bl3;
        }
        catch (IOException iOException) {
            return false;
        }
    }

    @Override
    @NotNull
    public BFClientAntiCheat.Stage getStage() {
        return BFClientAntiCheat.Stage.STARTUP;
    }
}

