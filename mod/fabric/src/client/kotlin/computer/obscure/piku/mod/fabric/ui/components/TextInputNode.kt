package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUINode
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUITextInput
import computer.obscure.piku.mod.fabric.ui.classes.UIEvent
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import computer.obscure.piku.mod.fabric.ui.menu.PikuEditBox
import computer.obscure.piku.mod.fabric.utils.toNativeComponent
import net.kyori.adventure.text.Component
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.input.CharacterEvent
import net.minecraft.client.input.KeyEvent

class TextInputNode : TextNode() {
    var placeholder: String? = "Input..."
    var placeholderComponent: Component? = Component.text(placeholder ?: "")
    var editBox: EditBox? = null

    var maxLength: Int = 32

    // The base hooks for this component.
    // Can not be overriden through Luau!!
    fun onBaseInput(event: UIEvent, node: LuaUINode) {
        onInput?.invoke(event, node)
    }
    fun onBaseConfirm(event: UIEvent, node: LuaUINode) {
        onConfirm?.invoke(event, node)
    }

    // The hooks for this component that can be
    // overridden through Luau.
    var onInput: ((UIEvent, LuaUINode) -> Unit)? = null
    var onConfirm: ((UIEvent, LuaUINode) -> Unit)? = null

    var renderMode = RenderMode.PLACEHOLDER

    private fun ensureEditBox(ctx: MeasureContext): EditBox {
        val box = editBox ?: PikuEditBox(
            ctx.textRenderer,
            layoutX.toInt(),
            layoutY.toInt(),
            measuredWidth.toInt(),
            measuredHeight.toInt(),
            Component.empty().toNativeComponent()
        ).apply {
            setMaxLength(maxLength)
            isBordered = false
            value = rawText ?: ""
            setResponder { newValue ->
                rawText = newValue
                originText = Component.text(newValue)

                checkPlaceholder()
                // this seems subpar
                onBaseInput(
                    UIEvent.TextConfirm(newValue),
                    LuaUITextInput(this@TextInputNode)
                )
            }
            placeholder?.let {
                setHint(Component.text(it).toNativeComponent())
            }
//            isVisible = false
        }.also { editBox = it }

        box.x = layoutX.toInt()
        box.y = layoutY.toInt()
        box.setWidth(measuredWidth.toInt())
        box.setHeight(measuredHeight.toInt())
        return box
    }

    override fun onBaseFocus(event: UIEvent, node: LuaUINode) {
        checkPlaceholder()
        super.onBaseFocus(event, node)
    }

    override fun onBaseUnfocus(event: UIEvent, node: LuaUINode) {
        checkPlaceholder()
        super.onBaseUnfocus(event, node)
    }

    fun checkPlaceholder() {
        if ((placeholder != null && rawText.isNullOrEmpty())) {
            renderMode = RenderMode.PLACEHOLDER
        } else {
            renderMode = RenderMode.TEXT
        }
    }

    override fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        if ((placeholder != null && rawText.isNullOrEmpty()) && renderMode == RenderMode.PLACEHOLDER)
            return ctx.textRenderer.width(placeholder!!) * resolvedScaleX to (ctx.textRenderer.lineHeight.toFloat() - 1) * resolvedScaleY
        return super.measureContent(ctx)
    }

    fun handleKeyPressed(event: KeyEvent): Boolean {
        if (event.isConfirmation) {
            // this seems subpar
            onBaseConfirm(
                UIEvent.TextConfirm(rawText ?: ""),
                LuaUITextInput(this@TextInputNode)
            )
        }
        return editBox?.keyPressed(event) ?: false
    }
    fun handleCharTyped(event: CharacterEvent): Boolean =
        editBox?.charTyped(event) ?: false

    override fun drawContent(graphics: GuiGraphicsExtractor, ctx: MeasureContext) {
        val box = ensureEditBox(ctx)
        box.isFocused = this.focused
        box.extractWidgetRenderState(graphics, box.cursorPosition, 0, 0f)
        // stop bothering with our own text renderer
        // minecraft already does it better by default
//        if ((placeholder != null && rawText.isNullOrEmpty()) && renderMode == RenderMode.PLACEHOLDER) {
//            drawLine(placeholder!!, graphics, ctx)
//            return
//        }
//        return super.drawContent(graphics, ctx)
    }

    enum class RenderMode {
        PLACEHOLDER,
        TEXT
    }
}