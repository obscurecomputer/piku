package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.scripting.old.ui.LuaUINode
import computer.obscure.piku.mod.fabric.ui.classes.UIEvent
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import computer.obscure.piku.mod.fabric.ui.menu.PikuEditBox
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.input.CharacterEvent
import net.minecraft.client.input.KeyEvent
import net.minecraft.network.chat.Component

class TextInputNode(text: Component) : TextNode(text) {
    constructor(text: String) : this(Component.literal(text))

    var placeholder: String? = "Input..."
    var placeholderComponent: Component? = Component.literal(placeholder ?: "")
    private var editBox: EditBox? = null

    var renderMode = RenderMode.PLACEHOLDER

    private fun ensureEditBox(ctx: MeasureContext): EditBox {
        val box = editBox ?: PikuEditBox(
            ctx.textRenderer,
            layoutX.toInt(),
            layoutY.toInt(),
            measuredWidth.toInt(),
            measuredHeight.toInt(),
            Component.empty()
        ).apply {
            isBordered = false
            value = rawText ?: ""
            setResponder { newValue ->
                rawText = newValue
                text = Component.literal(newValue)
                println("HELLO new value $rawText")
            }
            placeholder?.let {
                setHint(Component.literal(it))
            }
            isVisible = false
        }.also { editBox = it }

        box.x = layoutX.toInt()
        box.y = layoutY.toInt()
        return box
    }

    override fun onBaseFocus(event: UIEvent, node: LuaUINode) {
        PikuClient.LOGGER.info("Render mode = TEXT")
        renderMode = RenderMode.TEXT
        super.onBaseFocus(event, node)
    }

    override fun onBaseUnfocus(event: UIEvent, node: LuaUINode) {
        if ((placeholder != null && rawText == null)) {
            PikuClient.LOGGER.info("Render mode = PLACEHOLDER")
            renderMode = RenderMode.PLACEHOLDER
        }
        super.onBaseUnfocus(event, node)
    }

    override fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        if ((placeholder != null && rawText == null) && renderMode == RenderMode.PLACEHOLDER)
            return ctx.textRenderer.width(placeholder!!) * resolvedScaleX to (ctx.textRenderer.lineHeight.toFloat() - 1) * resolvedScaleY
        return super.measureContent(ctx)
    }

    fun handleKeyPressed(event: KeyEvent): Boolean =
        editBox?.keyPressed(event) ?: false
    fun handleCharTyped(event: CharacterEvent): Boolean =
        editBox?.charTyped(event) ?: false

    override fun drawContent(graphics: GuiGraphicsExtractor, ctx: MeasureContext) {
        val box = ensureEditBox(ctx)
        box.isFocused = this.focused
        box.extractWidgetRenderState(graphics, 0, 0, 0f)
        if ((placeholder != null && rawText == null) && renderMode == RenderMode.PLACEHOLDER) {
            drawLine(placeholder!!, graphics, ctx)
            return
        }
        return super.drawContent(graphics, ctx)
    }

    enum class RenderMode {
        PLACEHOLDER,
        TEXT
    }
}