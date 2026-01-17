package computer.obscure.piku.client.scripting.api

import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import computer.obscure.piku.client.scripting.api.util.unwrap
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import net.minecraft.component.ComponentType
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LoreComponent
import net.minecraft.component.type.NbtComponent
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtByte
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtDouble
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtFloat
import net.minecraft.nbt.NbtInt
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtLong
import net.minecraft.nbt.NbtShort
import net.minecraft.nbt.NbtString
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.text.TextCodecs
import net.minecraft.util.Identifier
import java.util.Optional

class LuaComponents(
    private val stack: ItemStack
) : TwineNative() {

    @TwineNativeFunction("get")
    fun getComponent(id: String): Any? {
        val type = Registries.DATA_COMPONENT_TYPE.get(Identifier.of(id))

        val value = stack.components.get(type) ?: return null
        return valueToLua(value)
    }

    @TwineNativeFunction
    fun set(id: String, value: Any?) {
        val type = Registries.DATA_COMPONENT_TYPE
            .get(Identifier.of(id))

        if (value == null) {
            stack.remove(type)
            return
        }

        val converted = luaToValue(type, value) ?: return

        @Suppress("UNCHECKED_CAST")
        stack.set(type as ComponentType<Any>, converted)
    }

    @TwineNativeFunction
    fun remove(id: String) {
        val type = Registries.DATA_COMPONENT_TYPE.get(Identifier.of(id))

        stack.remove(type)
    }

    private fun jsonToText(json: String): Text {
        val element = JsonParser.parseString(json)
        return TextCodecs.CODEC
            .parse(JsonOps.INSTANCE, element)
            .getOrThrow()
    }

    private fun textToJson(text: Text): String =
        TextCodecs.CODEC
            .encodeStart(JsonOps.INSTANCE, text)
            .getOrThrow()
            .toString()

    private fun valueToLua(value: Any): Any? = when (value) {
        is Optional<*> -> value.unwrap()

        is Int, is Boolean, is Double, is Float, is String -> value
        is Text -> textToJson(value)
        is LoreComponent -> value.lines.map(::textToJson)

        is NbtComponent -> {
            val nbt = value.copyNbt()
            LuaNbtCompound(
                nbt.keys.associateWith { key ->
                    nbtToLua(nbt.get(key)!!)?.unwrap()
                }
            )
        }

        else -> value.toString()
    }

    private fun nbtToLua(nbt: NbtElement): Any? = when (nbt) {
        is NbtByte -> nbt.byteValue() != 0.toByte()
        is NbtShort -> nbt.shortValue()
        is NbtInt -> nbt.intValue()
        is NbtLong -> nbt.longValue()
        is NbtFloat -> nbt.floatValue()
        is NbtDouble -> nbt.doubleValue()
        is NbtString -> nbt.asString()

        is NbtCompound -> LuaNbtCompound(
            nbt.keys.associateWith { key ->
                nbtToLua(nbt.get(key)!!)
            }
        )

        is NbtList -> nbt.map(::nbtToLua)

        else -> null
    }

    private fun luaToValue(
        type: ComponentType<*>?,
        value: Any
    ): Any? = when (type) {

        DataComponentTypes.CUSTOM_NAME ->
            (value as? String)?.let(::jsonToText)

        DataComponentTypes.LORE ->
            (value as? List<*>)?.mapNotNull {
                (it as? String)?.let(::jsonToText)
            }?.let { LoreComponent(it) }

        DataComponentTypes.CUSTOM_MODEL_DATA ->
            value as? Int

        else -> null
    }
}