package me.znotchill.piku.common.scripting.api

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import kotlin.collections.iterator

class LuaEventData(fields: Map<String, Any?>) {
    val table: LuaTable = LuaTable()

    init {
        for ((key, value) in fields) {
            table[key] = when (value) {
                is LuaValue -> value
                else -> LuaValue.userdataOf(value)
            }
        }
    }

    operator fun get(key: String) = table[key]
    operator fun set(key: String, value: Any?) {
        table[key] = when (value) {
            is LuaValue -> value
            else -> LuaValue.userdataOf(value)
        }
    }
}