package computer.obscure.piku.common.ui.events

data class OpacityEvent(
    override var delay: Long,
    override val targetId: String,
    val opacity: Float,
    val durationSeconds: Double,
    val easing: String,
) : UIEvent()