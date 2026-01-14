package computer.obscure.piku.client.ui.components

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import computer.obscure.piku.common.ui.components.Line
import computer.obscure.piku.common.ui.components.rebuildRuns

class LineRenderer : UIComponent<Line>() {

    override fun drawContent(component: Line, context: DrawContext, instance: MinecraftClient) {
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