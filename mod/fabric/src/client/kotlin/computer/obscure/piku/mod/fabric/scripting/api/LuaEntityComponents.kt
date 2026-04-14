package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.mod.fabric.scripting.api.components.LuaComponent
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity

class LuaEntityComponents(
    private val entity: Entity
) : TwineNative(), LuaComponent {

    @TwineFunction("get")
    fun get(id: String): Any? {
        val type = BuiltInRegistries.DATA_COMPONENT_TYPE
            .get(Identifier.parse(id))
            .map { it.value() }
            .orElse(null) ?: return null

        val value = entity.get(type) ?: return null
        return valueToLua(value)
    }
}