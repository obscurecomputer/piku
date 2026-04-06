package computer.obscure.piku.core.scripting.engine

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction

class ScriptLoader(private val scripts: Map<String, String>) : TwineNative("require") {
    @TwineFunction
    fun load(name: String): String {
        return scripts[name]
            ?: scripts[name.replace(".", "/")]
            ?: error("Script not found: $name")
    }
}