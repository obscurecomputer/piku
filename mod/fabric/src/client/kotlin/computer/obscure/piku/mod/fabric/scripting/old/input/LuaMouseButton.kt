package computer.obscure.piku.mod.fabric.scripting.old.input

import computer.obscure.piku.mod.fabric.MouseButton
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineProperty

class LuaMouseButton(button: MouseButton) : TwineNative() {
    @TwineProperty
    val name = button.name
    @TwineProperty
    val index = button.index
}