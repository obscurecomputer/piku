package computer.obscure.piku.mod.fabric.ui.classes

import computer.obscure.piku.mod.fabric.MouseButton
import computer.obscure.piku.mod.fabric.scripting.api.input.LuaMouseButton
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineProperty

sealed interface UIEvent {
    data class Pointer(
        @TwineProperty
        val screenX: Float,
        @TwineProperty
        val screenY: Float,
        @TwineProperty
        val localX: Float,
        @TwineProperty
        val localY: Float,
        @TwineProperty
        val buttonIndex: Int,

        @TwineProperty
        val button: LuaMouseButton = LuaMouseButton(button = MouseButton.fromIndex(buttonIndex))
    ) : TwineNative(), UIEvent

    data class Controller(
        @TwineProperty
        val controllerId: String,
        @TwineProperty
        val bindingName: String
    ) : TwineNative(), UIEvent

    data object Manual : TwineNative(), UIEvent
}