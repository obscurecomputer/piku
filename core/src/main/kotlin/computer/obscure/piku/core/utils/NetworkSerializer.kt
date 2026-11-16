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

//val json = Json { ignoreUnknownKeys = true }
//
//data class SerializableTypeEntry<T : Any>(
//    val klass: KClass<T>,
//    val typeName: String,
//    val serializer: KSerializer<T>,
//    val toLua: (T) -> TwineNative
//)
//
//val pikuSerializableTypes: List<SerializableTypeEntry<*>> = listOf(
//    SerializableTypeEntry(Vec3::class, "vec3", Vec3.serializer()) { it },
//    SerializableTypeEntry(
//        Vec2::class, "vec2",
//        Vec2.serializer()
//    ) { it },
//
//    SerializableTypeEntry(
//        Color::class, "color",
//        Color.serializer()
//    ) { it }
//)
//
//val typeNameToEntry: Map<String, SerializableTypeEntry<*>> =
//    pikuSerializableTypes.associateBy { it.typeName }
//
//val classToEntry: Map<KClass<*>, SerializableTypeEntry<*>> =
//    pikuSerializableTypes.associateBy { it.klass }
//
//@OptIn(InternalSerializationApi::class)
//fun Any?.toJson(): String = when (this) {
//    null -> "null"
//    is String -> JsonPrimitive(this).toString()
//    is Number -> this.toString()
//    is Boolean -> this.toString()
////    is WireRef -> JsonObject(
////        mapOf(
////            "__type" to JsonPrimitive("ref"),
////            "kind" to JsonPrimitive(refType),
////            "id" to JsonPrimitive(refId)
////        )
////    ).toString()
//    is PikuSerializable -> this.toJsonElement().toString()
//    is PikuEncodable -> {
//        @Suppress("UNCHECKED_CAST")
//        json.encodeToJsonElement(this.serializer() as KSerializer<Any>, this).toString()
//    }
//    is Map<*, *> -> {
//        val entries = this.entries.joinToString(",") { (k, v) -> "\"$k\":${v.toJson()}" }
//        "{$entries}"
//    }
//    is List<*> -> {
//        val entries = this.joinToString(",") { it.toJson() }
//        "[$entries]"
//    }
//    else -> {
//        val klass = this::class
//        val entry = classToEntry[this::class]
//        if (entry != null) {
//            @Suppress("UNCHECKED_CAST")
//            val serializer = entry.serializer as KSerializer<Any>
//            val encoded = json.encodeToJsonElement(serializer, this) as JsonObject
//            JsonObject(buildMap {
//                put("__type", JsonPrimitive(entry.typeName))
//                putAll(encoded)
//            }).toString()
//        } else {
//            val serializer = klass.serializerOrNull() as? KSerializer<Any>
//
//            if (serializer != null) {
//                json.encodeToJsonElement(serializer, this).toString()
//            } else {
//                JsonPrimitive(this.toString()).toString()
//            }
//        }
//    }
//}
//
//fun jsonToKotlin(value: JsonElement): Any? {
//    return when (value) {
//        is JsonObject -> {
//            val typeName = (value["__type"] ?: value["valueName"])?.jsonPrimitive?.contentOrNull
//            val entry = typeName?.let { typeNameToEntry[it] }
//            if (typeName == "ref") {
//                val kind = value["kind"]!!.jsonPrimitive.content
//                val uuid = UUID.fromString(value["id"]!!.jsonPrimitive.content)
//
//                return Platform.current?.resolve(kind, uuid)
//            }
//            if (entry != null) {
//                @Suppress("UNCHECKED_CAST")
//                val serializer = entry.serializer as KSerializer<Any>
//                val decoded = json.decodeFromJsonElement(serializer, value)
//                @Suppress("UNCHECKED_CAST")
//                (entry.toLua as (Any) -> TwineNative)(decoded)
//            } else {
//                value.entries.associate { (k, v) -> k to jsonToKotlin(v) }
//            }
//        }
//
//        is JsonArray -> value.map { jsonToKotlin(it) }
//        is JsonPrimitive -> when {
//            value.isString -> value.content
//            value.booleanOrNull != null -> value.boolean
//            value.doubleOrNull != null -> value.double
//            else -> null
//        }
//
//        is JsonNull -> null
//        else -> null
//    }
//}
//fun jsonStringToKotlin(json: String): Any? {
//    return jsonToKotlin(Json.parseToJsonElement(json))
//}