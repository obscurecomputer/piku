package computer.obscure.piku.mod.fabric.ui.menu

import computer.obscure.piku.mod.fabric.ui.UIRenderer
import computer.obscure.piku.mod.fabric.ui.classes.UIEvent
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
    var escapeClose = false
    var blur = false

    override fun isPauseScreen(): Boolean = true
    override fun shouldCloseOnEsc() = escapeClose

    override fun extractBlurredBackground(graphics: GuiGraphicsExtractor) {
        if (blur)
            super.extractBlurredBackground(graphics)
    }

    fun addRoot(node: UINode) {
        roots.add(node)
        UIRenderer.registerNode(node)
    }

    fun removeRoot(node: UINode) {
        roots.remove(node)
        UIRenderer.deindexTree(node)
    }

    fun getAllNodes(): List<UINode> {
        val result = mutableListOf<UINode>()
        fun collect(node: UINode) {
            result.add(node)
            node.children.forEach { collect(it) }
        }
        roots.forEach { collect(it) }
        return result
    }

    private fun hitTest(node: UINode, x: Float, y: Float): UINode? {
        for (child in node.children.asReversed()) {
            hitTest(child, x, y)?.let {
                return it
            }
        }
        if (node.visible && node.containsPoint(x, y))
            return node
        return null
    }

    private fun hitTestRoots(x: Float, y: Float): UINode? {
        for (root in roots.asReversed()) {
            hitTest(root, x, y)?.let { return it }
        }
        return null
    }

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

    override fun mouseMoved(x: Double, y: Double) {
        super.mouseMoved(x, y)
    }

    override fun mouseClicked(event: MouseButtonEvent, doubleClick: Boolean): Boolean {
        val hit = hitTestRoots(event.x().toFloat(), event.y().toFloat())
        if (hit != null) {
            val uiEvent = UIEvent.Pointer(
                screenX = event.x().toFloat(),
                screenY = event.y().toFloat(),
                localX = event.x().toFloat() - hit.layoutX,
                localY = event.y().toFloat() - hit.layoutY,
                buttonIndex = event.button()
            )
            hit.activated = true
            hit.onPress?.invoke(uiEvent)
            return true
        }
        return super.mouseClicked(event, doubleClick)
    }

    override fun mouseReleased(event: MouseButtonEvent): Boolean {
        val hit = hitTestRoots(event.x().toFloat(), event.y().toFloat())
        val uiEvent = UIEvent.Pointer(
            screenX = event.x().toFloat(),
            screenY = event.y().toFloat(),
            localX = event.x().toFloat() - (hit?.layoutX ?: 0f),
            localY = event.y().toFloat() - (hit?.layoutY ?: 0f),
            buttonIndex = event.button()
        )
        getAllNodes().forEach {
            if (it.activated) {
                it.activated = false
                it.onRelease?.invoke(uiEvent)
                return true
            }
        }
        return super.mouseReleased(event)
    }

    override fun mouseDragged(event: MouseButtonEvent, dx: Double, dy: Double): Boolean {
        return super.mouseDragged(event, dx, dy)
    }

    override fun onClose() {
        super.onClose()
    }
}