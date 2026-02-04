package computer.obscure.piku.core.scripting.api

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

    private fun toLuaValue(value: Any?): LuaValue = when (value) {
        null -> LuaValue.NIL
        is LuaValue -> value
        is String -> LuaValue.valueOf(value)
        is Int -> LuaValue.valueOf(value)
        is Long -> LuaValue.valueOf(value.toInt())
        is Double -> LuaValue.valueOf(value)
        is Float -> LuaValue.valueOf(value.toDouble())
        is Boolean -> LuaValue.valueOf(value)
        else -> LuaValue.userdataOf(value)
    }
}