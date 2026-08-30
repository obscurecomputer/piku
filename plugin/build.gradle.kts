import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.changelog")
    id("org.jetbrains.intellij.platform")
}

dependencies {
    intellijPlatform {
        bundledPlugin("org.jetbrains.kotlin")
        intellijIdea("2026.2")
        testFramework(TestFrameworkType.Platform)

        pluginVerifier()
    }
    implementation("computer.obscure.piku:script-api:1.7.0")
}

intellijPlatform {
    instrumentCode = false
}