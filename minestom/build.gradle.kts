plugins {
    kotlin("jvm") version "2.3.0"
}

group = "me.znotchill.piku"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.znotchill.me/repository/maven-releases/")
    maven(url = "https://central.sonatype.com/repository/maven-snapshots/") {
        content {
            includeModule("net.minestom", "minestom")
            includeModule("net.minestom", "testing")
        }
    }
}

dependencies {
    implementation(project(":common"))
    testImplementation(kotlin("test"))
    implementation("io.netty:netty-buffer:4.1.111.Final")
    implementation("io.netty:netty-common:4.1.111.Final")
    implementation("net.minestom:minestom:2025.12.19-1.21.10")
    implementation("me.znotchill:blossom:1.4.7")
    implementation("io.github.xn32:json5k:0.3.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("net.kyori:adventure-text-minimessage:4.24.0")
    implementation("net.kyori:adventure-api:4.24.0")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("org.luaj:luaj-jse:3.0.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(25)
}