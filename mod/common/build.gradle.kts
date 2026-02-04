plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    kotlin("jvm")
}

architectury {
    common("fabric", "forge", "neoforge")
}

dependencies {
    implementation(project(":core"))
    val minecraft_version = rootProject.extra["minecraft_version"] as String

    minecraft("net.minecraft:minecraft:$minecraft_version")
    mappings(loom.officialMojangMappings())

    modImplementation("dev.architectury:architectury:${rootProject.extra["architectury_version"]}")

    implementation("org.luaj:luaj-jse:${rootProject.extra["luaj_version"]}")
    implementation("computer.obscure:twine:${rootProject.extra["twine_version"]}")
    implementation("net.kyori:adventure-text-minimessage:${rootProject.extra["adventure_version"]}")
    implementation("net.kyori:adventure-platform-mod-shared:6.7.0")

}