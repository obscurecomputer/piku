package computer.obscure.piku.mod.common.ui.components

import computer.obscure.piku.core.ui.components.Line
import computer.obscure.piku.core.ui.components.rebuildRuns
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics

class LineRenderer : UIComponent<Line>() {

    override fun drawContent(component: Line, context: GuiGraphics, instance: Minecraft) {
        if (component.props.geometryDirty) {
            component.rebuildRuns()
        }

        val color = component.props.color.toArgb()

        for (run in component.runs) {
            context.fill(
                (run.x),
                (run.y),
                (run.x) + run.w,
                (run.y) + run.h,
                color
            )
        }
    }
}