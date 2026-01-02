plugins {
    kotlin("jvm") version "2.2.20"
}

group = "me.znotchill.piku"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.obscure.computer/repository/maven-releases/")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.netty:netty-buffer:4.1.111.Final")
    implementation("io.netty:netty-common:4.1.111.Final")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.joml:joml:1.10.8")
    implementation("org.luaj:luaj-jse:${project.property("luaj_version")}")
    implementation("computer.obscure:twine:${project.property("twine_version")}")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}