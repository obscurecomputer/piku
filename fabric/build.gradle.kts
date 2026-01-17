import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.20"
    id("fabric-loom") version "1.11-SNAPSHOT"
    kotlin("kapt") version "2.2.20"
    id("maven-publish")
    kotlin("plugin.serialization") version "2.2.20"
    id("com.modrinth.minotaur") version "2.+"
}

group = project.property("group")!!
version = project.property("fabric_mod_version")!!

base {
    archivesName.set(project.property("archives_base_name") as String)
}

val targetJavaVersion = 21
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}
kotlin {
    jvmToolchain(targetJavaVersion)
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register("piku") {
            sourceSet("main")
            sourceSet("client")
        }
    }

    mixin {
        add(sourceSets.main.get(), "piku.client.mixins.json")
        useLegacyMixinAp = false
    }
}

sourceSets {
    named("main") {
        extensions.extraProperties["refMap"] = "piku.refmap.json"
    }
    named("client") {
        java.srcDirs("src/client/java", "src/client/kotlin")
        resources.srcDirs("src/client/resources")
    }
}

kapt {
    correctErrorTypes = true
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

repositories {
    mavenLocal()
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com/")
    }
    maven("https://jitpack.io")
    maven("https://repo.obscure.computer/repository/maven-releases/")
}

dependencies {
    // To change the versions see the gradle.properties file
    implementation(project(":common"))
    include(project(":common"))
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.property("kotlin_loader_version")}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    modImplementation("com.terraformersmc:modmenu:${project.property("modmenu_version")}")
    implementation("org.luaj:luaj-jse:${project.property("luaj_version")}")
    include("org.luaj:luaj-jse:${project.property("luaj_version")}")
    implementation("computer.obscure:twine:${project.property("twine_version")}")
    include("computer.obscure:twine:${project.property("twine_version")}")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version") as Any,
            "loader_version" to project.property("loader_version") as Any,
            "kotlin_loader_version" to project.property("kotlin_loader_version") as Any
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    // If Javadoc is generated, this must be specified in that task too.
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName}" }
    }
    manifest {
        attributes(
            "MixinConfigs" to "piku.mixins.json"
        )
    }
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])

            group
            artifactId = "fabric"
            version
        }
    }

    repositories {
        maven {
            name = "obscurerepo"
            url = uri("https://repo.obscure.computer/repository/maven-releases/")
            credentials {
                username = findProperty("obscureUsername") as String? ?: System.getenv("OBSCURE_MAVEN_USER")
                password = findProperty("obscurePassword") as String? ?: System.getenv("OBSCURE_MAVEN_PASS")
            }
        }

        mavenLocal()
    }
}

tasks.named<Jar>("sourcesJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
tasks.withType<ProcessResources>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("piku")
    versionNumber.set(project.version.toString())
    versionType.set(
        if (project.version.toString().contains("SNAPSHOT") ||
            project.version.toString().contains("beta")) {
            "beta"
        } else {
            "release"
        }
    )
    uploadFile.set(tasks.jar)
    gameVersions.addAll("1.21.10")
    loaders.addAll("fabric")
    changelog.set(System.getenv("CHANGELOG") ?: "Automated release from CI")
}
