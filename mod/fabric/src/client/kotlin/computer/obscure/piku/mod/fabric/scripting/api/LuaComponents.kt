package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.mod.fabric.scripting.api.components.LuaComponent
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

class LuaComponents(
    private val stack: ItemStack
) : TwineNative(), LuaComponent {

    @TwineFunction("get")
    fun getComponent(id: String): Any? {
        val type = BuiltInRegistries.DATA_COMPONENT_TYPE
            .get(Identifier.parse(id))
            .map { it.value() }
            .orElse(null) ?: return null

        val value = stack.get(type) ?: return null
        return valueToLua(value)
    }

    @TwineFunction
    fun set(id: String, value: Any?) {
        val type = BuiltInRegistries.DATA_COMPONENT_TYPE
            .get(Identifier.parse(id))
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

    @TwineFunction
    fun remove(id: String) {
        val type = BuiltInRegistries.DATA_COMPONENT_TYPE
            .get(Identifier.parse(id))
            .map { it.value() }
            .orElse(null) ?: return

        stack.remove(type)
    }
}