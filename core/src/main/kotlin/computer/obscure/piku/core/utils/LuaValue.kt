package computer.obscure.piku.core.utils

import me.znotchill.kiwi.generated.Vec2
import computer.obscure.piku.core.classes.Vec3
import computer.obscure.piku.core.serialization.PikuSerializable
import computer.obscure.twine.TwineNative
import computer.obscure.twine.TwineValue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*
import me.znotchill.kiwi.generated.Color
import kotlin.reflect.KClass

val json = Json { ignoreUnknownKeys = true }


data class SerializableTypeEntry<T : Any>(
    val klass: KClass<T>,
    val typeName: String,
    val serializer: KSerializer<T>,
    val toLua: (T) -> TwineNative
)

val pikuSerializableTypes: List<SerializableTypeEntry<*>> = listOf(
    SerializableTypeEntry(Vec3::class, "vec3", Vec3.serializer()) { it.toLuaInstance() },
    SerializableTypeEntry(
        Vec2::class, "vec2",
        Vec2.serializer()
    ) { TwineValue(it, "vec2") },

    SerializableTypeEntry(
        Color::class, "color",
        Color.serializer()
    ) { TwineValue(it, "color") }
)

val typeNameToEntry: Map<String, SerializableTypeEntry<*>> =
    pikuSerializableTypes.associateBy { it.typeName }

val classToEntry: Map<KClass<*>, SerializableTypeEntry<*>> =
    pikuSerializableTypes.associateBy { it.klass }

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
    else -> {
        val entry = classToEntry[this::class]
        if (entry != null) {
            @Suppress("UNCHECKED_CAST")
            val serializer = entry.serializer as KSerializer<Any>
            val encoded = json.encodeToJsonElement(serializer, this) as JsonObject
            JsonObject(buildMap {
                put("__type", JsonPrimitive(entry.typeName))
                putAll(encoded)
            }).toString()
        } else {
            JsonPrimitive(this.toString()).toString()
        }
    }
}

fun jsonToKotlin(value: JsonElement): Any? {
    return when {
        value is JsonObject -> {
            val typeName = value["__type"]?.jsonPrimitive?.contentOrNull
            val entry = typeName?.let { typeNameToEntry[it] }
            if (entry != null) {
                @Suppress("UNCHECKED_CAST")
                val serializer = entry.serializer as KSerializer<Any>
                val decoded = json.decodeFromJsonElement(serializer, value)
                @Suppress("UNCHECKED_CAST")
                (entry.toLua as (Any) -> TwineNative)(decoded)
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