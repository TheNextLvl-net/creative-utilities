import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.2"
    id("io.papermc.hangar-publish-plugin") version "0.1.4"
    id("de.eldoria.plugin-yml.paper") version "0.8.0"
    id("com.modrinth.minotaur") version "2.+"
}

group = "net.thenextlvl.utilities"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.compileJava {
    options.release.set(21)
}

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.thenextlvl.net/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")

    implementation("net.thenextlvl.core:files:4.0.0-pre1")
    implementation("net.thenextlvl.core:paper:3.0.0-pre1")
    implementation("net.thenextlvl.version-checker:modrinth-paper:1.0.1")
    implementation("net.thenextlvl:i18n:1.2.0")

    implementation("dev.faststats.metrics:bukkit:0.16.0")
    implementation("org.bstats:bstats-bukkit:3.2.1")
}

tasks.shadowJar {
    relocate("org.bstats", "${rootProject.group}.metrics")
    archiveBaseName.set("creative-utilities")
    minimize()
}

paper {
    name = "CreativeUtilities"
    main = "net.thenextlvl.utilities.UtilitiesPlugin"
    apiVersion = "1.21.5"
    provides = listOf("Builders-Utilities")
    website = "https://thenextlvl.net"
    authors = listOf("Ktar5", "Arcaniax", "NonSwag")
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    foliaSupported = true
    serverDependencies {
        register("FastAsyncWorldEdit") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
    permissions {
        register("builders.util.advanced-fly")
        register("builders.util.air-placing")
        register("builders.util.banner")
        register("builders.util.color")
        register("builders.util.gui")
        register("builders.util.night-vision")
        register("builders.util.no-clip")
        register("builders.util.pottery-designer")

        register("builders.util.slabs") { default = BukkitPluginDescription.Permission.Default.TRUE }
        register("builders.util.tpgm3") { default = BukkitPluginDescription.Permission.Default.TRUE }
        register("builders.util.trapdoor") { default = BukkitPluginDescription.Permission.Default.TRUE }

        val notice = "backwards compatibility for the new permission schema"

        register("additions.command.no-clip") {
            children = listOf("builders.util.no-clip")
            description = notice
        }
        register("builders.util.advancedfly") {
            children = listOf("builders.util.advanced-fly")
            description = notice
        }
        register("builders.util.nightvision") {
            children = listOf("builders.util.night-vision")
            description = notice
        }
        register("builders.util.noclip") {
            children = listOf("builders.util.no-clip")
            description = notice
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(listOf("--add-reads", "creative.utilities.main=ALL-UNNAMED"))
}

tasks.withType<Test>().configureEach {
    jvmArgs("--add-reads", "creative.utilities.main=ALL-UNNAMED")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--add-reads", "creative.utilities.main=ALL-UNNAMED")
}

tasks.withType<Javadoc>().configureEach {
    val options = options as StandardJavadocDocletOptions
    options.addStringOption("-add-reads", "creative.utilities.main=ALL-UNNAMED")
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
        changelog = System.getenv("CHANGELOG")
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

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("zAcZq5oV")
    changelog = System.getenv("CHANGELOG")
    versionType = if (isRelease) "release" else "beta"
    uploadFile.set(tasks.shadowJar)
    gameVersions.set(versions)
    syncBodyFrom.set(rootProject.file("README.md").readText())
    loaders.add("paper")
    loaders.add("folia")
    dependencies {
        optional.project("fastasyncworldedit")
    }
}
