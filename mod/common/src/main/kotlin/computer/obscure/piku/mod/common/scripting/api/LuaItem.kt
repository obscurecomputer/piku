package computer.obscure.piku.mod.common.scripting.api

import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
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

    @TwineNativeProperty
    val id: String
        get() = BuiltInRegistries.ITEM.getId(stack.item).toString()

    @TwineNativeProperty
    val count: Int
        get() = stack.count

    @TwineNativeProperty
    val data: LuaComponents
        get() = LuaComponents(stack)
}