configurations.all {
    exclude(group = "net.kyori", module = "adventure-text-logger-slf4j")
}

apply(plugin = "dev.architectury.loom")

plugins {
    id("com.gradleup.shadow")
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("dev.architectury.loom")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

configurations {
    val common by creating {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    getByName("compileClasspath").extendsFrom(common)
    getByName("runtimeClasspath").extendsFrom(common)
    getByName("developmentNeoForge").extendsFrom(common)

    val shadowBundle by creating {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

repositories {
    maven {
        name = "NeoForged"
        url = uri("https://maven.neoforged.net/releases")
    }

    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
}
dependencies {
    neoForge("net.neoforged:neoforge:${rootProject.extra["neoforge_version"]}")
    implementation("thedarkcolour:kotlinforforge-neoforge:6.0.0")

    modImplementation("dev.architectury:architectury-neoforge:${rootProject.extra["architectury_version"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation(project(":core"))

    modImplementation(include("net.kyori:adventure-platform-neoforge:6.7.0")!!)

    "common"(project(path = ":mod:common", configuration = "namedElements")) { isTransitive = false }
    "shadowBundle"(project(path = ":mod:common", configuration = "transformProductionNeoForge"))
}

tasks.processResources {
    val expandProps = mapOf(
        "version" to project.version,
        "minecraft_version" to rootProject.extra["minecraft_version"],
        "neoforge_version" to rootProject.extra["neoforge_version"]
    )
    inputs.properties(expandProps)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(expandProps)
    }
}

tasks.shadowJar {
    configurations = listOf(project.configurations.getByName("shadowBundle"))
    archiveClassifier.set("dev-shadow")
}

tasks.remapJar {
    inputFile.set(tasks.shadowJar.get().archiveFile)
}