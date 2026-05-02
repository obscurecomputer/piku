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
        get() = state.values.entries.associate { (property, value) ->
            @Suppress("UNCHECKED_CAST")
            property.name to (property as Property<Comparable<Any>>)
                .getName(value as Comparable<Any>)
        }

    @Suppress("UNCHECKED_CAST")
    @TwineFunction
    fun getProperty(name: String): String? {
        val entry = state.values.entries.find { it.key.name == name } ?: return null
        val prop = entry.key as Property<Comparable<Any>>
        return prop.getName(entry.value as Comparable<Any>)
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