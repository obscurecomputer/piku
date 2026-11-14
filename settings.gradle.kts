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

// todo: decide if we wanna maintain paper or leave it as-is
// since all development will steer towards minestom for performance
// and minimal-ity
//include("server:paper")
include("mod:fabric")
