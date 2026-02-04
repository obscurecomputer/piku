import org.gradle.api.tasks.compile.JavaCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("dev.architectury.loom") version "1.11-SNAPSHOT" apply false
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

architectury {
    minecraft = minecraft_version
}

allprojects {
    group = group
    version = mod_version
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")
    apply(plugin = "java-library")

    extensions.configure<BasePluginExtension> {
        archivesName.set("$archives_name-${project.name}")
    }

    repositories {
        mavenCentral()
        maven { url = uri("https://maven.architectury.dev/") }

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
        val loom = project.extensions.getByName("loom")

        "minecraft"("net.minecraft:minecraft:$minecraft_version")

        val mappingsMethod = loom.javaClass.getMethod("officialMojangMappings")
        "mappings"(mappingsMethod.invoke(loom))
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