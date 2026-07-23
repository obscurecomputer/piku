plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "9.4.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    `java-library`
    `maven-publish`
}

group = project.property("group")!!
version = project.property("paper_api_version")!!

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.znotchill.me/releases/")
    maven("https://repo.obscure.computer/repository/maven-releases/")

    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven { url = uri("https://repo.codemc.io/repository/maven-releases/") }
    maven { url = uri("https://repo.codemc.io/repository/maven-snapshots/") }
}

dependencies {
    implementation(project(":core"))
    compileOnly("io.papermc.paper:paper-api:${project.property("paper_version")}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.github.retrooper:packetevents-spigot:2.12.1")
}

tasks.shadowJar {
    relocate("io.github.retrooper.packetevents", "computer.obscure.piku.paper.libs.packetevents")
    relocate("com.github.retrooper.packetevents", "computer.obscure.piku.paper.libs.packetevents2")
}

tasks {
  runServer {
    // Configure the Minecraft version for our task.
    // This is the only required configuration besides applying the plugin.
    // Your plugin's jar (or shadowJar if present) will be used automatically.
    minecraftVersion("1.21.11")
  }
}

val targetJavaVersion = 25
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            group
            artifactId = "paper"
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