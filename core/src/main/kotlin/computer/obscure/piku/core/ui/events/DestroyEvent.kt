package computer.obscure.piku.core.ui.events

data class DestroyEvent(
    override var delay: Long,
    override val targetId: String
) : UIEvent()