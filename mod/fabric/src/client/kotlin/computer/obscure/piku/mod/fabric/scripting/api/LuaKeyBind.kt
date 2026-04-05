package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.mod.fabric.InputHandler
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import net.minecraft.client.Minecraft

class LuaKeyBind(
    @TwineNativeProperty
    val name: String,
    @TwineNativeProperty
    val boundKey: String,

    val isDown: Boolean,

    @TwineNativeProperty
    val category: String,
    @TwineNativeProperty
    val isUnbound: Boolean,
    @TwineNativeProperty
    val isDefault: Boolean,
) : TwineNative() {
    val mc: Minecraft = Minecraft.getInstance()
    val mapping = mc.options.keyMappings.find { it.name == this.name }

    @TwineNativeProperty("isDown")
    var internalIsDown: Boolean
        get() = mapping?.isDown ?: false
        set(value) {
            mapping?.isDown = value
        }

    @TwineNativeFunction
    fun once() {
        mapping?.let {
            setDown(true)
            InputHandler.queueInputUp(this)
        }
    }

    @TwineNativeFunction
    fun activate() {
        mapping?.isDown = true
    }

    @TwineNativeFunction
    fun deactivate() {
        mapping?.isDown = false
    }

    @TwineNativeFunction
    fun setDown(value: Boolean) {
        mapping?.isDown = value
    }

    @TwineNativeFunction
    fun toggle() {
        mapping?.isDown = !mapping.isDown
    }

    @TwineNativeFunction("tostring")
    override fun toString(): String {
        return "keybind[name=$name,boundKey=$boundKey,isDown=$isDown,category=$category,isUnbound=$isUnbound,isDefault=$isDefault]"
    }
}