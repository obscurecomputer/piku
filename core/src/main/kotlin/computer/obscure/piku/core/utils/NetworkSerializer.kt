package computer.obscure.piku.core.utils

import computer.obscure.piku.core.scripting.platform.Dto
import computer.obscure.piku.core.scripting.platform.DtoConvertible
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlinx.serialization.serializerOrNull

object NetworkSerializer {
    val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "__type"
    }


    @OptIn(InternalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    fun toJson(value: Any?): JsonElement = when (value) {
        null -> JsonNull
        is JsonElement -> value
        is String -> JsonPrimitive(value)
        is Number -> JsonPrimitive(value)
        is Boolean -> JsonPrimitive(value)
        is DtoConvertible ->
            json.encodeToJsonElement(Dto.serializer(), value.toDto())
        is Dto -> json.encodeToJsonElement(Dto.serializer(), value)
        is Map<*, *> -> JsonObject(
            value.entries.associate { (k, v) ->
                k.toString() to toJson(v)
            }
        )
        is Iterable<*> -> JsonArray(
            value.map {
                toJson(it)
            }
        )
        else -> {
            val s = value::class.serializerOrNull() as? KSerializer<Any>
            if (s != null)
                json.encodeToJsonElement(s, value)
            else JsonPrimitive(value.toString())
        }
    }

    fun toKotlin(value: JsonElement): Any? = when (value) {
        is JsonNull -> null
        is JsonArray -> value.map { toKotlin(it) }
        is JsonObject ->
            if ("__type" in value)
                json.decodeFromJsonElement(Dto.serializer(), value).fromDto()
            else
                value.mapValues { toKotlin(it.value) }
        is JsonPrimitive -> when {
            value.isString -> value.content
            value.booleanOrNull != null -> value.boolean
            else -> value.doubleOrNull
        }
    }

    fun toKotlin(value: String) =
        toKotlin(json.parseToJsonElement(value))
}

fun Any?.toJson(): JsonElement {
    return NetworkSerializer.toJson(this)
}

fun JsonElement.toKotlin(): Any? {
    return NetworkSerializer.toKotlin(this)
}

fun JsonElement.asString() = NetworkSerializer.json.encodeToString(this)
