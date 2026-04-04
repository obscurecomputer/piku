plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    kotlin("jvm")
}

repositories {
    mavenLocal()
}

dependencies {
    implementation(project(":core"))
    val minecraft_version = rootProject.extra["minecraft_version"] as String

//    modCompileOnly("dev.architectury:architectury:${rootProject.extra["architectury_version"]}")

    compileOnly("org.spongepowered:mixin:0.8.5")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    implementation("org.luaj:luaj-jse:${rootProject.extra["luaj_version"]}")

    implementation("computer.obscure:twine:${rootProject.extra["twine_version"]}")

    compileOnly("net.kyori:adventure-text-minimessage:${rootProject.extra["adventure_version"]}")
    compileOnly("net.kyori:adventure-platform-mod-shared:6.7.0")

    compileOnly("net.fabricmc:fabric-loader:${rootProject.extra["fabric_loader_version"]}")
}

tasks.named("transformProductionFabric") {
    enabled = false
}

tasks.named("transformProductionNeoForge") {
    enabled = false
}

loom {
    @Suppress("UnstableApiUsage")
    mixin {
        useLegacyMixinAp = false
    }
}