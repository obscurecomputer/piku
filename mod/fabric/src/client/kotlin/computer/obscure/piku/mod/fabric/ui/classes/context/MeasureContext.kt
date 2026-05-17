package computer.obscure.piku.mod.fabric.ui.classes.context

import net.minecraft.client.gui.Font

data class MeasureContext(
    val textRenderer: Font,
    val parentWidth: Float,
    val parentHeight: Float,
    val scaleFactor: Float
)