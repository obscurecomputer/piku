plugins {
    kotlin("jvm") version "2.4.10" apply false
    kotlin("plugin.serialization") version "2.4.10" apply false
}

val jvmVersion = "2.4.10"

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.znotchill.me/releases/") {
            content {
                includeGroup("me.znotchill")
                includeGroup("me.znotchill.luau")
                includeGroup("me.znotchill.endergine")
            }
        }
    }

    dependencies {
        add("implementation", "me.znotchill:kiwi:${project.property("kiwi_version")}")
        add("implementation", "computer.obscure:endergine:${project.property("endergine_version")}")
        add("implementation", "org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:${jvmVersion}")
        add("implementation", "org.jetbrains.kotlin:kotlin-scripting-common:${jvmVersion}")
        add("implementation", "org.jetbrains.kotlin:kotlin-scripting-jvm:${jvmVersion}")
        add("implementation", "org.jetbrains.kotlin:kotlin-scripting-jvm-host:${jvmVersion}")
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