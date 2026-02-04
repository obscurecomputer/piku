
plugins {
    kotlin("jvm") version "2.3.0" apply false
    kotlin("plugin.serialization") version "2.3.0" apply false
}

subprojects {
    val targetVersion =
        if (project.path.contains("minestom") || project.name.contains("minestom")) 25
        else 21

    tasks.withType<JavaCompile>().configureEach {
        options.release.set(targetVersion)
    }

    plugins.withId("org.jetbrains.kotlin.jvm") {
        val kotlinExtension = extensions.getByName("kotlin") as org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
        kotlinExtension.jvmToolchain(targetVersion)
    }
}