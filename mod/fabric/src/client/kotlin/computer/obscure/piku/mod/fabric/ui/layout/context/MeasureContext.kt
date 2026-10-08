package computer.obscure.piku.mod.fabric.ui.layout.context

import net.minecraft.client.gui.Font

data class MeasureContext(
    val textRenderer: Font,
    val parentWidth: Float,
    val parentHeight: Float,
    val scaleFactor: Float,
    val parentScale: Double = 1.0
)