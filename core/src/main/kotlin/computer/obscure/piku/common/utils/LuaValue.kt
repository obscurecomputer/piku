package computer.obscure.piku.common.utils

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

fun LuaValue.toJson(): String {
    return when {
        istable() -> {
            val table = checktable()
            val entries = mutableListOf<String>()

            var k = LuaValue.NIL
            while (true) {
                val n = table.next(k)
                if (n.arg1().isnil()) break
                k = n.arg1()
                val v = n.arg(2)

                entries += "\"${k.tojstring()}\":${v.toJson()}"
            }

            "{${entries.joinToString(",")}}"
        }

        isstring() -> "\"${tojstring()}\""
        isnumber() -> tojstring()
        isboolean() -> toboolean().toString()
        isnil() -> "null"
        else -> "\"${tojstring()}\""
    }
}

fun jsonToLua(value: JsonElement): LuaValue {
    return when {
        value.isJsonObject -> {
            val table = LuaTable()
            for ((k, v) in value.asJsonObject.entrySet()) {
                table[k] = jsonToLua(v)
            }
            table
        }

        value.isJsonArray -> {
            val table = LuaTable()
            value.asJsonArray.forEachIndexed { i, v ->
                table[i + 1] = jsonToLua(v)
            }
            table
        }

        value.isJsonPrimitive -> {
            val p = value.asJsonPrimitive
            when {
                p.isBoolean -> LuaValue.valueOf(p.asBoolean)
                p.isNumber -> LuaValue.valueOf(p.asDouble)
                p.isString -> LuaValue.valueOf(p.asString)
                else -> LuaValue.NIL
            }
        }

        value.isJsonNull -> LuaValue.NIL
        else -> LuaValue.NIL
    }
}

fun jsonStringToLua(json: String): LuaValue {
    val element = JsonParser.parseString(json)
    return jsonToLua(element)
}