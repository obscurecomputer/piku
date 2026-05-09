package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction

class LuaBaseGenerics : TwineNative() {
    @TwineFunction
    fun type(value: Any?): String {
        return when (value) {
            null -> "nil"
            is TwineNative -> value.resolvedName.ifEmpty { "userdata" }
            is Boolean -> "boolean"
            is Long, is Int, is Double, is Float -> "number"
            is String -> "string"
            is Map<*, *> -> "table"
            is List<*> -> "table"
            else -> "userdata"
        }
    }
}