package computer.obscure.piku.core.serialization

import computer.obscure.twine.TwineNative
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
abstract class PikuSerializable {
    abstract val typeName: String
    abstract fun toLuaInstance(): TwineNative

    fun toJsonElement(): JsonElement {
//        val entry = typeNameToEntry[typeName]
//            ?: error("Unregistered type: $typeName")
//
//        @Suppress("UNCHECKED_CAST")
//        val serializer = entry.serializer as KSerializer<PikuSerializable>
//
//        val encoded = json.encodeToJsonElement(serializer, this) as JsonObject
        return JsonNull
//        return JsonObject(buildMap {
//            put("__type", JsonPrimitive(typeName))
//            putAll(encoded)
//        })
    }
}