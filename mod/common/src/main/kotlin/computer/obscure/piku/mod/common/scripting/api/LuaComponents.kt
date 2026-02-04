package computer.obscure.piku.mod.common.scripting.api

import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import computer.obscure.piku.mod.common.scripting.api.util.unwrap
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.ByteTag
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.FloatTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.LongTag
import net.minecraft.nbt.ShortTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.item.component.CustomModelData
import net.minecraft.world.item.component.ItemLore
import java.util.Optional

class LuaComponents(
    private val stack: ItemStack
) : TwineNative() {

    @TwineNativeFunction("get")
    fun getComponent(id: String): Any? {
        val type = BuiltInRegistries.DATA_COMPONENT_TYPE
            .get(ResourceLocation.parse(id))
            .map { it.value() }
            .orElse(null) ?: return null

        val value = stack.get(type) ?: return null
        return valueToLua(value)
    }

    @TwineNativeFunction
    fun set(id: String, value: Any?) {
        val type = BuiltInRegistries.DATA_COMPONENT_TYPE
            .get(ResourceLocation.parse(id))
            .map { it.value() }
            .orElse(null) ?: return

        if (value == null) {
            stack.remove(type)
            return
        }

        val converted = luaToValue(type, value) ?: return

        @Suppress("UNCHECKED_CAST")
        stack.set(type as DataComponentType<Any>, converted)
    }

    @TwineNativeFunction
    fun remove(id: String) {
        val type = BuiltInRegistries.DATA_COMPONENT_TYPE
            .get(ResourceLocation.parse(id))
            .map { it.value() }
            .orElse(null) ?: return

        stack.remove(type)
    }

    private fun jsonToText(json: String): Component {
        val element = JsonParser.parseString(json)
        return ComponentSerialization.CODEC
            .parse(JsonOps.INSTANCE, element)
            .getOrThrow()
    }

    private fun textToJson(text: Component): String =
        ComponentSerialization.CODEC
            .encodeStart(JsonOps.INSTANCE, text)
            .getOrThrow()
            .toString()

    private fun valueToLua(value: Any): Any? = when (value) {
        is Optional<*> -> value.unwrap()
        is Int, is Boolean, is Double, is Float, is String -> value
        is Component -> textToJson(value)
        is ItemLore -> value.lines.map(::textToJson)
        is CustomData -> nbtToLua(value.copyTag())
        is CustomModelData -> mapOf(
            "floats" to value.floats(),
            "flags" to value.flags(),
            "strings" to value.strings(),
            "colors" to value.colors()
        )
        else -> value.toString()
    }

    private fun nbtToLua(nbt: Tag): Any? = when (nbt) {
        is ByteTag -> nbt.asByte().unwrap() != 0.toByte()
        is ShortTag -> nbt.asShort()
        is IntTag -> nbt.asInt()
        is LongTag -> nbt.asLong()
        is FloatTag -> nbt.asFloat()
        is DoubleTag -> nbt.asDouble()
        is StringTag -> nbt.asString()
        is CompoundTag -> nbt.keySet().associateWith { key -> nbtToLua(nbt.get(key)!!) }
        is ListTag -> nbt.map(::nbtToLua)

        else -> null
    }

    private fun luaToValue(
        type: DataComponentType<*>?,
        value: Any
    ): Any? = when (type) {
        DataComponents.CUSTOM_NAME -> (value as? String)?.let(::jsonToText)

        DataComponents.LORE -> {
            (value as? List<*>)?.mapNotNull {
                (it as? String)?.let(::jsonToText)
            }?.let { ItemLore(it) }
        }

        DataComponents.CUSTOM_MODEL_DATA -> {
            when (value) {
                is Number -> {
                    CustomModelData(listOf(value.toFloat()), listOf(), listOf(), listOf())
                }

                is Map<*, *> -> {
                    CustomModelData(
                        (value["floats"] as? List<Float>) ?: listOf(),
                        (value["flags"] as? List<Boolean>) ?: listOf(),
                        (value["strings"] as? List<String>) ?: listOf(),
                        (value["colors"] as? List<Int>) ?: listOf()
                    )
                }

                else -> null
            }
        }

        else -> null
    }
}