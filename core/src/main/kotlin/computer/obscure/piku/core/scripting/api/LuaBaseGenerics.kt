package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.utils.toTwineString

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

    @TwineFunction("tostring")
    fun toString(value: Any?): String {
        return when (value) {
            null -> "null"
            is String -> "value"
            is Map<*, *> -> "{${value.keys.joinToString(", ")}}"
            is List<*> -> "[${value.joinToString(", ")}]"
            is Double -> value.toTwineString()
            else -> "$value"
        }
    }
}