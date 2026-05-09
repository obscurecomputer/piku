package computer.obscure.piku.core.utils

import computer.obscure.piku.core.classes.Vec2
import computer.obscure.piku.core.classes.Vec3
import computer.obscure.piku.core.serialization.PikuSerializable
import computer.obscure.piku.core.ui.classes.UIColor
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*

val json = Json { ignoreUnknownKeys = true }

// todo: move this / make it better
val pikuSerializableTypes: Map<String, KSerializer<out PikuSerializable>> = mapOf(
    "color" to UIColor.serializer(),
    "vec2" to Vec2.serializer(),
    "vec3" to Vec3.serializer(),
)


fun Any?.toJson(): String = when (this) {
    null -> "null"
    is String -> JsonPrimitive(this).toString()
    is Number -> this.toString()
    is Boolean -> this.toString()
    is PikuSerializable -> this.toJsonElement().toString()
    is Map<*, *> -> {
        val entries = this.entries.joinToString(",") { (k, v) -> "\"$k\":${v.toJson()}" }
        "{$entries}"
    }
    is List<*> -> {
        val entries = this.joinToString(",") { it.toJson() }
        "[$entries]"
    }
    else -> JsonPrimitive(this.toString()).toString()
}

fun jsonToKotlin(value: JsonElement): Any? {
    return when {
        value is JsonObject -> {
            val typeName = value["__type"]?.jsonPrimitive?.contentOrNull
            val target = typeName?.let { pikuSerializableTypes[it] }
            if (target != null) {
                json.decodeFromJsonElement(target, value).toLuaInstance()
            } else {
                value.entries.associate { (k, v) -> k to jsonToKotlin(v) }
            }
        }
        value is JsonArray -> value.map { jsonToKotlin(it) }
        value is JsonPrimitive -> when {
            value.isString -> value.content
            value.booleanOrNull != null -> value.boolean
            value.doubleOrNull != null -> value.double
            else -> null
        }
        value is JsonNull -> null
        else -> null
    }
}

fun jsonStringToKotlin(json: String): Any? {
    return jsonToKotlin(Json.parseToJsonElement(json))
}