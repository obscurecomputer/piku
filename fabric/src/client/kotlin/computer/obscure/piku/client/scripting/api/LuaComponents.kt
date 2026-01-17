package computer.obscure.piku.client.scripting.api

import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import net.minecraft.component.ComponentType
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LoreComponent
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.text.TextCodecs
import net.minecraft.util.Identifier

class LuaComponents(
    private val stack: ItemStack
) : TwineNative("components") {

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

    private fun valueToLua(value: Any): Any = when (value) {
        is Int, is Boolean, is Double, is Float, is String -> value

        is Text -> textToJson(value)
        is LoreComponent -> value.lines.map(::textToJson)

        else -> value.toString()
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