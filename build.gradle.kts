plugins {
    id("dev.architectury.loom") version "1.10-SNAPSHOT"
}

group = "red.vuis"
version = "0.7.1.2b-hotfix4"
description = "FrontCore companion utility mod for BlockFront. Credit to the OG author: forteus19 (https://github.com/forteus19/Front-Utilities)."

base {
    archivesName.set("FrontCore")
}

val bfVersion = "0.7.1.2b"
val bfNamedJar = file("bf/$bfVersion-named.jar")
val bfOriginalJar = file("bf/$bfVersion-original.jar")
val bfLibJar = file("bf/bflib-$bfVersion.jar")

fun hasAnyBfJar() = bfNamedJar.exists() || bfOriginalJar.exists()
val bfMergedTiny = file("bf/$bfVersion-merged.tiny")

repositories {
    maven("https://maven.fabricmc.net")
    maven("https://maven.neoforged.net/releases")
    exclusiveContent {
        forRepository {
            maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven")
        }
        filter {
            includeGroup("software.bernie.geckolib")
        }
    }
}

val remapperTools by configurations.creating

dependencies {
    minecraft("net.minecraft:minecraft:1.21.1")
    mappings(loom.layered {
        mappings("net.fabricmc:yarn:1.21.1+build.3:v2")
        mappings("dev.architectury:yarn-mappings-patch-neoforge:1.21+build.4")
    })
    neoForge("net.neoforged:neoforge:21.1.219")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    if (hasAnyBfJar()) {
        val compileJar = if (bfNamedJar.exists()) bfNamedJar else bfOriginalJar
        modCompileOnly(files(compileJar))
        modRuntimeOnly(files(compileJar))
        if (bfLibJar.exists()) {
            compileOnly(files(bfLibJar))
        }
    } else {
        logger.warn("Missing BlockFront jar in bf/. Add at least $bfVersion-original.jar (or $bfVersion-named.jar).")
    }

    modCompileOnly("software.bernie.geckolib:geckolib-neoforge-1.21.1:4.7.3")
    modRuntimeOnly("software.bernie.geckolib:geckolib-neoforge-1.21.1:4.7.3")
    compileOnly("com.demonwav.mcdev:annotations:2.1.0")
    remapperTools("net.fabricmc:tiny-remapper:0.10.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile>() {
    options.release = 21
}

val remapForRelease by tasks.registering(JavaExec::class) {
    group = "build"
    description = "Remaps FrontCore from BF named symbols to BF official runtime symbols."
    dependsOn("remapJar")
    classpath = remapperTools
    mainClass.set("net.fabricmc.tinyremapper.Main")
    onlyIf {
        bfMergedTiny.exists() && bfNamedJar.exists() && bfOriginalJar.exists()
    }
    doFirst {
        val inputJar = tasks.named("remapJar").get().outputs.files.singleFile
        val outputJar = layout.buildDirectory.file("libs/${rootProject.name}-${project.version}-release.jar").get().asFile

        val argsList = mutableListOf(
            inputJar.absolutePath,
            outputJar.absolutePath,
            bfMergedTiny.absolutePath,
            "named",
            "official",
            "--ignoreConflicts",
            bfNamedJar.absolutePath,
            bfOriginalJar.absolutePath
        )
        if (bfLibJar.exists()) {
            argsList.add(bfLibJar.absolutePath)
        }
        args = argsList
    }
    doLast {
        val releaseJar = layout.buildDirectory.file("libs/${rootProject.name}-${project.version}-release.jar").get().asFile
        val defaultJar = layout.buildDirectory.file("libs/${rootProject.name}-${project.version}.jar").get().asFile
        releaseJar.copyTo(defaultJar, overwrite = true)
    }
}

tasks.named("build") {
    finalizedBy(remapForRelease)
}
