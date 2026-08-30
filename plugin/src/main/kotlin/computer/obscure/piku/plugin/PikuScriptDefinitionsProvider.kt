package computer.obscure.piku.plugin

import java.io.File
import kotlin.script.experimental.intellij.ScriptDefinitionsProvider

class PikuScriptDefinitionsProvider : ScriptDefinitionsProvider {
    override val id: String
        get() = "piku"

    override fun getDefinitionClasses(): Iterable<String> {
        return listOf(
            "computer.obscure.piku.mod.fabric.scripting.PikuScript"
        )
    }

    override fun getDefinitionsClassPath(): Iterable<File> {
        return emptyList()
    }

    override fun useDiscovery(): Boolean {
        return false
    }
}