package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.mod.fabric.InputHandler
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative
import net.minecraft.client.Minecraft

class LuaKeyBind(
    @TwineProperty
    val name: String,
    @TwineProperty
    val boundKey: String,

    val isDown: Boolean,

    @TwineProperty
    val category: String,
    @TwineProperty
    val isUnbound: Boolean,
    @TwineProperty
    val isDefault: Boolean,
) : TwineNative() {
    val mc: Minecraft = Minecraft.getInstance()
    val mapping = mc.options.keyMappings.find { it.name == this.name }

    @TwineProperty("isDown")
    var internalIsDown: Boolean
        get() = mapping?.isDown ?: false
        set(value) {
            mapping?.isDown = value
        }

    @TwineFunction
    fun once() {
        mapping?.let {
            setDown(true)
            InputHandler.queueInputUp(this)
        }
    }

    @TwineFunction
    fun activate() {
        mapping?.isDown = true
    }

    @TwineFunction
    fun deactivate() {
        mapping?.isDown = false
    }

    @TwineFunction
    fun setDown(value: Boolean) {
        mapping?.isDown = value
    }

    @TwineFunction
    fun toggle() {
        mapping?.isDown = !mapping.isDown
    }

    @TwineFunction("tostring")
    override fun toString(): String {
        return "keybind[name=$name,boundKey=$boundKey,isDown=$isDown,category=$category,isUnbound=$isUnbound,isDefault=$isDefault]"
    }
}