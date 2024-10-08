import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

group = "net.thenextlvl.utilities"
version = "1.1.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.compileJava {
    options.release.set(21)
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    compileOnly("net.thenextlvl.core:annotations:2.0.1")
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")

    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("net.thenextlvl.core:adapters:1.0.9")
    implementation("net.thenextlvl.core:paper:1.4.1")
    implementation("net.thenextlvl.core:files:1.0.5")
    implementation("net.thenextlvl.core:i18n:1.0.18")

    annotationProcessor("org.projectlombok:lombok:1.18.34")
}

tasks.shadowJar {
    relocate("org.bstats", "${rootProject.group}.metrics")
    archiveBaseName.set("creative-utilities")
    minimize()
}

paper {
    name = "CreativeUtilities"
    main = "net.thenextlvl.utilities.UtilitiesPlugin"
    apiVersion = "1.21"
    provides = listOf("Builders-Utilities")
    website = "https://thenextlvl.net"
    authors = listOf("Ktar5", "Arcaniax", "NonSwag")
    foliaSupported = true
    serverDependencies {
        register("FastAsyncWorldEdit") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
    permissions {
        register("builders.util.trapdoor") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("builders.util.slabs") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("builders.util.air-placing") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("builders.util.nightvision") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("builders.util.noclip") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("builders.util.advancedfly") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("builders.util.tpgm3") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}

val versionString: String = project.version as String
val isRelease: Boolean = !versionString.contains("-pre")

val versions: List<String> = (property("game.versions") as String)
    .split(",")
    .map { it.trim() }

hangarPublish { // docs - https://docs.papermc.io/misc/hangar-publishing
    publications.register("paper") {
        id.set("CreativeUtilities")
        version.set(versionString)
        channel.set(if (isRelease) "Release" else "Snapshot")
        apiKey.set(System.getenv("HANGAR_API_TOKEN"))
        platforms.register(Platforms.PAPER) {
            jar.set(tasks.shadowJar.flatMap { it.archiveFile })
            platformVersions.set(versions)
        }
    }
}
