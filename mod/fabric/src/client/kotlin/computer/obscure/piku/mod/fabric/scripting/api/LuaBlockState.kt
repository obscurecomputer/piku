package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property

class LuaBlockState(
    val state: BlockState
) : TwineNative() {
    @TwineProperty
    val properties: Map<String, String>
        get() = state.properties.associate { property ->
            @Suppress("UNCHECKED_CAST")
            val prop = property as Property<Comparable<Any>>
            prop.name to prop.getName(state.getValue(property))
        }

    @Suppress("UNCHECKED_CAST")
    @TwineFunction
    fun getProperty(name: String): String? {
        val property = state.properties.find { it.name == name } ?: return null

        val prop = property as Property<Comparable<Any>>
        return prop.getName(state.getValue(property))
    }

    @TwineProperty
    val block: LuaBlock
        get() = LuaBlock(state.block)

    @TwineProperty
    val isAir: Boolean
        get() = state.isAir

    @TwineProperty
    val lightEmission: Int
        get() = state.lightEmission

    @TwineProperty
    val isSolid: Boolean
        get() = state.isSolid

    @TwineProperty
    val hasBlockEntity: Boolean
        get() = state.hasBlockEntity()

    @TwineProperty
    val isSignalSource: Boolean
        get() = state.isSignalSource
}