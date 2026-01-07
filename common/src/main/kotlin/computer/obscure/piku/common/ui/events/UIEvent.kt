package computer.obscure.piku.common.ui.events

import computer.obscure.piku.common.ui.UIWindow

sealed class UIEvent {
    abstract val targetId: String
    abstract var delay: Long
}