package computer.obscure.piku.core.ui.events


data class ProgressEvent(
    override var delay: Long,
    override val targetId: String,
    val progress: Float,
    val durationSeconds: Double,
    val easing: String,
) : UIEvent()