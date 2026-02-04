plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    kotlin("jvm")
}

architectury {
    common("fabric", "forge", "neoforge")
}

dependencies {
    implementation(project(":common"))

    modImplementation("net.fabricmc:fabric-loader:${rootProject.extra["fabric_loader_version"]}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${rootProject.extra["fabric_api_version"]}")
    modImplementation("dev.architectury:architectury-fabric:${rootProject.extra["architectury_version"]}")
}