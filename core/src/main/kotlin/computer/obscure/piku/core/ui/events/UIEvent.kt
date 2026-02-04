package computer.obscure.piku.core.ui.events

sealed class UIEvent {
    abstract val targetId: String
    abstract var delay: Long
}