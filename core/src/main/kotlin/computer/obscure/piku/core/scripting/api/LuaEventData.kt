package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.nativex.TwineNative
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import kotlin.collections.iterator

class LuaEventData(fields: Map<String, Any?>) {
    val table: LuaTable = LuaTable()

    init {
        for ((key, value) in fields) {
            table[key] = toLuaValue(value)
        }
    }

    operator fun get(key: String) = table[key]
    operator fun set(key: String, value: Any?) {
        table[key] = toLuaValue(value)
    }

    fun toLuaValue(value: Any?): LuaValue {
        val result = when (value) {
            is TwineNative -> value.table
            null -> LuaValue.NIL
            is LuaValue -> value
            is String -> LuaValue.valueOf(value)
            is Int -> LuaValue.valueOf(value)
            is Long -> LuaValue.valueOf(value.toInt())
            is Double -> LuaValue.valueOf(value)
            is Float -> LuaValue.valueOf(value.toDouble())
            is Boolean -> LuaValue.valueOf(value)
            is Map<*, *> -> {
                val childTable = LuaTable()
                value.forEach { (k, v) ->
                    childTable[toLuaValue(k)] = toLuaValue(v)
                }
                childTable
            }
            is LuaEventData -> {
                value.table
            }
            is List<*> -> {
                val childTable = LuaTable()
                value.forEachIndexed { index, item ->
                    childTable.set(index + 1, toLuaValue(item))
                }
                childTable
            }
            else -> LuaValue.userdataOf(value)
        }
        return result
    }
}