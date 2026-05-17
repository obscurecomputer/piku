pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://files.minecraftforge.net/maven/")
        maven("https://maven.neoforged.net/releases")
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "piku"
include("core")
include("server:minestom")
include("server:paper")
include("mod:fabric")
