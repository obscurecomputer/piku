pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://files.minecraftforge.net/maven/")
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "piku"
include("common")
include("minestom")
include("mod:fabric")
include("mod:common")