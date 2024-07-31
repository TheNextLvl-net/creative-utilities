plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
}

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
    archiveBaseName.set("creative-utilities")
    minimize()
}
