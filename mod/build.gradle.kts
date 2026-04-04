import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.tasks.compile.JavaCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("dev.architectury.loom") version "1.13.467" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("com.gradleup.shadow") version "8.3.6" apply false
    id("com.modrinth.minotaur") version "2.8.7" apply false
    `maven-publish`
    `java-library`
}

val minecraft_version: String by project
val archives_name: String by project
val mod_version: String by project
val group: String by project

allprojects {
    group = group
    version = mod_version
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "maven-publish")
    apply(plugin = "java-library")

    extensions.configure<BasePluginExtension> {
        archivesName.set("$archives_name-${project.name}")
    }

    val isCommon = project.path == ":mod:common"

    apply(plugin = "architectury-plugin")
    architectury {
        minecraft = minecraft_version
        if (isCommon) {
            common("fabric", "neoforge")
        }
    }

    configure<LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()
    }

    repositories {
        mavenCentral()
        maven { url = uri("https://maven.architectury.dev/") }
        maven("https://repo.spongepowered.org/repository/maven-public/")

        maven {
            name = "Terraformers"
            url = uri("https://maven.terraformersmc.com/")
        }
        maven("https://repo.obscure.computer/repository/maven-releases/")
    }

    afterEvaluate {
        val loom = project.extensions.findByName("loom")
        if (loom != null) {
            try {
                loom.javaClass.getMethod("silentMojangMappingsLicense").invoke(loom)
            } catch (e: Exception) { }
        }
    }

    dependencies {
        val loom = project.extensions.getByType<LoomGradleExtensionAPI>()

        "minecraft"("net.minecraft:minecraft:$minecraft_version")

        println(">>> Setting mappings for ${project.path}")
        "mappings"(loom.officialMojangMappings())
        println(">>> Mappings set: ${loom.officialMojangMappings()}")

        if (isCommon) {
            "compileOnly"("net.fabricmc:fabric-loader:${rootProject.extra["fabric_loader_version"]}")
            "modCompileOnly"("dev.architectury:architectury:${rootProject.extra["architectury_version"]}")
        }
    }

    extensions.configure<JavaPluginExtension> {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile>().configureEach {
        options.release.set(21)
    }

    extensions.configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                artifactId = "$archives_name-${project.name}"
                from(components["java"])
            }
        }
    }


    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KaptGenerateStubs>().configureEach {
        compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25)
    }
}