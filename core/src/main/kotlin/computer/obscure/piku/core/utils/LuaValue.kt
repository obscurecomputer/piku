package computer.obscure.piku.core.utils

import com.google.gson.JsonElement
import com.google.gson.JsonParser

fun Any?.toJson(): String {
    return when (this) {
        null -> "null"
        is String -> "\"${this}\""
        is Number -> this.toString()
        is Boolean -> this.toString()
        is Map<*, *> -> {
            val entries = this.entries.joinToString(",") { (k, v) ->
                "\"$k\":${v.toJson()}"
            }
            "{$entries}"
        }
        is List<*> -> {
            val entries = this.joinToString(",") { it.toJson() }
            "[$entries]"
        }
        else -> "\"${this}\""
    }
}

fun jsonToKotlin(value: JsonElement): Any? {
    return when {
        value.isJsonObject -> value.asJsonObject.entrySet()
            .associate { (k, v) -> k to jsonToKotlin(v) }
        value.isJsonArray -> value.asJsonArray
            .map { jsonToKotlin(it) }
        value.isJsonPrimitive -> value.asJsonPrimitive.let { p ->
            when {
                p.isBoolean -> p.asBoolean
                p.isNumber -> p.asDouble
                p.isString -> p.asString
                else -> null
            }
        }
        value.isJsonNull -> null
        else -> null
    }
}

fun jsonStringToKotlin(json: String): Any? {
    return jsonToKotlin(JsonParser.parseString(json))
}