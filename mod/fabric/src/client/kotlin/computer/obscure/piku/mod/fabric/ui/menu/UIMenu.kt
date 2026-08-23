package computer.obscure.piku.mod.fabric.ui.menu

import computer.obscure.piku.mod.fabric.ui.UIRenderer
import computer.obscure.piku.mod.fabric.ui.classes.context.LayoutContext
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import computer.obscure.piku.mod.fabric.ui.components.UINode
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component

class UIMenu(
    menuTitle: Component
) : Screen(menuTitle) {
    val roots = mutableListOf<UINode>()

    fun addRoot(node: UINode) {
        roots.add(node)
        UIRenderer.registerNode(node)
    }

    fun removeRoot(node: UINode) {
        roots.remove(node)
        UIRenderer.deindexTree(node)
    }

    override fun isPauseScreen(): Boolean = true

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        super.extractRenderState(graphics, mouseX, mouseY, a)

        val measureCtx = MeasureContext(
            this.font,
            width.toFloat(), height.toFloat(),
            minecraft.window.guiScale.toFloat()
        )
        val layoutCtx = LayoutContext(
            x = 0f, y = 0f,
            parentWidth = width.toFloat(),
            parentHeight = height.toFloat()
        )

        roots.forEach {
            it.measureSelf(measureCtx)
            it.layoutSelf(layoutCtx)
        }
        roots.forEach {
            it.drawSelf(graphics, measureCtx)
        }
    }

    override fun mouseClicked(event: MouseButtonEvent, doubleClick: Boolean): Boolean {
        return super.mouseClicked(event, doubleClick)
    }

    override fun mouseReleased(event: MouseButtonEvent): Boolean {
        return super.mouseReleased(event)
    }

    override fun mouseDragged(event: MouseButtonEvent, dx: Double, dy: Double): Boolean {
        return super.mouseDragged(event, dx, dy)
    }

    override fun onClose() {
        super.onClose()
    }
}