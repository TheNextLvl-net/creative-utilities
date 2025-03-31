import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta9"
    id("io.papermc.hangar-publish-plugin") version "0.1.3"
    id("de.eldoria.plugin-yml.paper") version "0.7.1"
}

group = "net.thenextlvl.utilities"
version = "1.2.1"

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
    compileOnly("org.projectlombok:lombok:1.18.36")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    implementation("org.bstats:bstats-bukkit:3.1.0")
    implementation("net.thenextlvl.core:adapters:2.0.2")
    implementation("net.thenextlvl.core:paper:2.0.3")
    implementation("net.thenextlvl.core:files:2.0.1")
    implementation("net.thenextlvl.core:i18n:1.0.21")

    annotationProcessor("org.projectlombok:lombok:1.18.36")
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
        register("builders.util.advancedfly") { default = BukkitPluginDescription.Permission.Default.OP }
        register("builders.util.air-placing") { default = BukkitPluginDescription.Permission.Default.TRUE }
        register("builders.util.banner") { default = BukkitPluginDescription.Permission.Default.OP }
        register("builders.util.color") { default = BukkitPluginDescription.Permission.Default.OP }
        register("builders.util.gui") { default = BukkitPluginDescription.Permission.Default.OP }
        register("builders.util.nightvision") { default = BukkitPluginDescription.Permission.Default.OP }
        register("builders.util.no-clip") { default = BukkitPluginDescription.Permission.Default.OP }
        register("builders.util.noclip") { default = BukkitPluginDescription.Permission.Default.OP }
        register("builders.util.pottery-designer") { default = BukkitPluginDescription.Permission.Default.OP }
        register("builders.util.slabs") { default = BukkitPluginDescription.Permission.Default.TRUE }
        register("builders.util.tpgm3") { default = BukkitPluginDescription.Permission.Default.OP }
        register("builders.util.trapdoor") { default = BukkitPluginDescription.Permission.Default.TRUE }
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
            dependencies {
                hangar("FastAsyncWorldEdit") {
                    required.set(false)
                }
            }
        }
    }
}
