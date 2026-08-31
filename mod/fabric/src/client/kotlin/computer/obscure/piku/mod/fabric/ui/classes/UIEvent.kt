package computer.obscure.piku.mod.fabric.ui.classes

import computer.obscure.piku.mod.fabric.MouseButton
import computer.obscure.piku.mod.fabric.ui.components.UINode
import computer.obscure.twine.TwineNative

sealed interface UIEvent {
    data class Pointer(
        val screenX: Float,
        val screenY: Float,
        val localX: Float,
        val localY: Float,
        val nodeX: Float = screenX - localX,
        val nodeY: Float = screenY - localY,
        val node: UINode?,
        val buttonIndex: Int,
        val button: MouseButton = MouseButton.fromIndex(buttonIndex)
    ) : UIEvent

    data class Hover(
        val screenX: Float,
        val screenY: Float,
        val localX: Float,
        val localY: Float,
        val node: UINode,
    ) : TwineNative(), UIEvent

    data class Controller(
        val controllerId: String,
        val bindingName: String
    ) : TwineNative(), UIEvent

    data object Manual : TwineNative(), UIEvent
    data object FocusDropped : TwineNative(), UIEvent
    data object HoverDropped : TwineNative(), UIEvent
}