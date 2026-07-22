plugins {
    kotlin("jvm") version "2.3.0" apply false
    kotlin("plugin.serialization") version "2.3.0" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.znotchill.me/repository/maven-releases/") {
            content {
                includeGroup("me.znotchill")
                includeGroup("me.znotchill.luau")
            }
        }
    }

    dependencies {
        add("implementation", "me.znotchill:kiwi:${project.property("kiwi_version")}")
    }

    val targetVersion = 25
    tasks.withType<JavaCompile>().configureEach {
        options.release.set(targetVersion)
    }
    plugins.withId("org.jetbrains.kotlin.jvm") {
        val kotlinExtension = extensions.getByName("kotlin") as org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
        kotlinExtension.jvmToolchain(targetVersion)
    }
}