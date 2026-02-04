plugins {
    kotlin("jvm") version "2.3.0"
    `java-library`
    `maven-publish`
}

group = project.property("group")!!
version = project.property("minestom_api_version")!!

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.znotchill.me/repository/maven-releases/")
    maven("https://repo.obscure.computer/repository/maven-releases/")
    maven(url = "https://central.sonatype.com/repository/maven-snapshots/") {
        content {
            includeModule("net.minestom", "minestom")
            includeModule("net.minestom", "testing")
        }
    }
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":core"))
    testImplementation(kotlin("test"))
    implementation("io.netty:netty-buffer:4.1.111.Final")
    implementation("io.netty:netty-common:4.1.111.Final")
    implementation("net.minestom:minestom:${project.property("minestom_version")}")
    implementation("me.znotchill:blossom:${project.property("blossom_version")}")
    implementation("io.github.xn32:json5k:0.3.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("net.kyori:adventure-text-minimessage:${project.property("adventure_version")}")
    implementation("net.kyori:adventure-api:${project.property("adventure_version")}")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("org.luaj:luaj-jse:${project.property("luaj_version")}")
    implementation("computer.obscure:twine:${project.property("twine_version")}")
}

tasks.test {
    useJUnitPlatform()
}
java {
    withSourcesJar()
    withJavadocJar()
}



publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            group
            artifactId = "minestom"
            version
        }
    }

    repositories {
        maven {
            name = "obscurerepo"
            url = uri("https://repo.obscure.computer/repository/maven-releases/")
            credentials {
                username = findProperty("obscureUsername") as String? ?: System.getenv("OBSCURE_MAVEN_USER")
                password = findProperty("obscurePassword") as String? ?: System.getenv("OBSCURE_MAVEN_PASS")
            }
        }

        mavenLocal()
    }
}