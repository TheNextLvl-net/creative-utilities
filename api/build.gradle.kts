plugins {
    id("java")
}

group = rootProject.group
version = rootProject.version

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
    withJavadocJar()
}

tasks.compileJava {
    options.release.set(21)
}

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.32")
    compileOnly("net.thenextlvl.core:annotations:2.0.1")
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")

    annotationProcessor("org.projectlombok:lombok:1.18.32")
}
