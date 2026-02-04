plugins {
    id("com.gradleup.shadow")
    id("com.modrinth.minotaur")
    kotlin("jvm")
    kotlin("plugin.serialization")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

configurations {
    val common by creating {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    getByName("compileClasspath").extendsFrom(common)
    getByName("runtimeClasspath").extendsFrom(common)
    getByName("developmentFabric").extendsFrom(common)

    val shadowBundle by creating {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${rootProject.extra["fabric_loader_version"]}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${rootProject.extra["kotlin_loader_version"]}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${rootProject.extra["fabric_api_version"]}")
    modImplementation("dev.architectury:architectury-fabric:${rootProject.extra["architectury_version"]}")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    modImplementation("com.terraformersmc:modmenu:${rootProject.extra["modmenu_version"]}")

    modImplementation(include("net.kyori:adventure-platform-fabric:6.7.0")!!)
    implementation(project(":core"))
    modImplementation(project(":core"))

    "common"(project(path = ":mod:common", configuration = "namedElements")) { isTransitive = false }
    "shadowBundle"(project(path = ":mod:common", configuration = "transformProductionFabric"))
}

loom {
    splitEnvironmentSourceSets()

    runs {
        named("client") {
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run")
        }
        named("server") {
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir("run/server")
        }
    }

    mods {
        register("piku") {
            sourceSet("main")
            sourceSet("client")
        }
    }
    mixin {
        useLegacyMixinAp = true
        add(sourceSets.main.get(), "piku.client.mixins.json")
    }
}

sourceSets {
    named("client") {
        java.srcDirs("src/client/java", "src/client/kotlin")
        resources.srcDirs("src/client/resources")
    }
}

tasks.shadowJar {
    configurations = listOf(project.configurations.getByName("shadowBundle"))
    archiveClassifier.set("dev-shadow")
}

tasks.remapJar {
    inputFile.set(tasks.shadowJar.get().archiveFile)
}

tasks.processResources {
    val expandProps = mapOf(
        "version" to project.version,
        "minecraft_version" to rootProject.extra["minecraft_version"],
        "loader_version" to rootProject.extra["fabric_loader_version"],
        "kotlin_loader_version" to rootProject.extra["kotlin_loader_version"]
    )
    inputs.properties(expandProps)
    filesMatching("fabric.mod.json") { expand(expandProps) }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
tasks.named<ProcessResources>("processClientResources") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("piku")
    versionNumber.set(project.version.toString())
    uploadFile.set(tasks.remapJar)
    gameVersions.addAll(rootProject.extra["minecraft_version"].toString())
    loaders.addAll("fabric")
}
