package computer.obscure.piku.mod.fabric.scripting

import computer.obscure.piku.mod.fabric.PikuClient
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.ScriptAcceptedLocation
import kotlin.script.experimental.api.ScriptCollectedData
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.api.acceptedLocations
import kotlin.script.experimental.api.asSuccess
import kotlin.script.experimental.api.baseClass
import kotlin.script.experimental.api.collectedAnnotations
import kotlin.script.experimental.api.compilerOptions
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.ide
import kotlin.script.experimental.api.importScripts
import kotlin.script.experimental.api.refineConfiguration
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

@KotlinScript(
    displayName = "Piku script", fileExtension = "piku.kts", compilationConfiguration = PikuScriptConfiguration::class
)
abstract class PikuScript

object PikuScriptConfiguration : ScriptCompilationConfiguration({
    baseClass(PikuScript::class)
    jvm {
        dependenciesFromCurrentContext(
            wholeClasspath = true
        )
    }
    defaultImports(Import::class)
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
    refineConfiguration {
        onAnnotations(Import::class) { context ->
            val annotations = context.collectedData
                ?.get(ScriptCollectedData.collectedAnnotations)
                ?: emptyList()

            val imports = annotations
                .filter { it.annotation.annotationClass.qualifiedName == Import::class.qualifiedName }
                .mapNotNull { getAnnotationValue(it.annotation) }

            val sources = imports
                .flatMap { import ->
                    import
                        .split(",")
                        .mapNotNull(::resolveScript)
                }
//            println("src: $sources")
//
            ScriptCompilationConfiguration(context.compilationConfiguration) {
                importScripts.append(sources)
            }.asSuccess()
        }
    }
    compilerOptions(
        "-jvm-target",
        "25"
    )
})

private fun getAnnotationValue(annotation: Annotation): String? {
    return runCatching {
        annotation.javaClass
            .getMethod("value")
            .invoke(annotation) as String
    }.getOrNull()
}

private fun resolveScript(name: String): SourceCode? {
    println("rewsolving")
    val engine = PikuClient.engine ?: return null

    val scriptName = if (name.endsWith(".piku.kts")) {
        name
    } else {
        "$name.piku.kts"
    }

    println("IMPORTING SCRIPT $name/$scriptName")
    println(engine.loadedScripts[scriptName])

    return engine.loadedScripts[scriptName]?.toScriptSource(scriptName)
}

@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
@Repeatable
annotation class Import(val value: String)