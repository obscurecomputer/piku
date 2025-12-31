plugins {
    kotlin("jvm") version "2.2.20"
}

group = "me.znotchill.piku"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.netty:netty-buffer:4.1.111.Final")
    implementation("io.netty:netty-common:4.1.111.Final")
    implementation("org.luaj:luaj-jse:3.0.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("dev.znci:twine:2.1.2")
    implementation("org.joml:joml:1.10.8")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}