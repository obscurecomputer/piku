package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ItemStack

class LuaItem : TwineNative() {
    lateinit var stack: ItemStack
        private set

    fun setStack(stack: ItemStack): LuaItem {
        check(!::stack.isInitialized) { "stack has already been set" }
        this.stack = stack
        return this
    }

    @TwineProperty
    val id: String
        get() = BuiltInRegistries.ITEM.getKey(stack.item).toString()

    @TwineProperty
    val count: Int
        get() = stack.count

    @TwineProperty
    val data: LuaComponents
        get() = LuaComponents(stack)
}