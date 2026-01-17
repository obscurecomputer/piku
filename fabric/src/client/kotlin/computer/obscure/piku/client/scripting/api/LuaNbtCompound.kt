package computer.obscure.piku.client.scripting.api

import computer.obscure.piku.client.scripting.api.util.unwrap
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.twine.annotations.TwineNativeFunction

class LuaNbtCompound(
    private val compound: Map<String, Any?>
) : TwineNative("nbt") {

    @TwineNativeFunction("get")
    fun getValue(key: String): Any? = compound[key]?.unwrap()

    @TwineNativeFunction("has")
    fun has(key: String): Boolean = compound.containsKey(key)

    @TwineNativeFunction("tostring")
    override fun toString(): String {
        if (compound.isEmpty()) return "nbt[]"

        val body = compound.entries.joinToString(", ") { (k, v) ->
            "$k=${formatValue(v)}"
        }

        return "nbt[$body]"
    }

    private fun formatValue(value: Any?): String = when (value) {
        null -> "nil"
        is String -> value
        is Boolean, is Number -> value.toString()
        is LuaNbtCompound -> value.toString()
        is List<*> -> value.joinToString(prefix = "[", postfix = "]") {
            formatValue(it)
        }
        else -> value.toString()
    }
}