package computer.obscure.piku.mod.common.ui.components

import computer.obscure.piku.core.ui.components.Text
import computer.obscure.piku.mod.common.ui.TextInterpolator
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics

class TextRenderer : UIComponent<Text>() {
    override fun drawContent(component: Text, context: GuiGraphics, instance: Minecraft) {
        val props = component.props
        val renderer = instance.font

        val x = props.pos.x
        val y = props.pos.y

        // interpolate dynamic variables & split into lines
        val interpolated = TextInterpolator.interpolate(props.text)

        val text = interpolated.colorIfAbsent(props.color.toTextColor())

        val mcText = MinecraftClientAudiences
            .of()
            .asNative(text)

        // measure and get the widest line
        val textWidth = renderer.width(mcText).toFloat()
        val textHeight = renderer.lineHeight.toFloat()

        val scaleX = props.textScale.x
        val scaleY = props.textScale.y

        val scaledTextWidth = textWidth * scaleX
        val scaledTextHeight = textHeight * scaleY

        val padding = props.padding

        val bgWidth = scaledTextWidth + padding.left + padding.right
        val bgHeight = scaledTextHeight + padding.top + padding.bottom

        props.backgroundColor?.let { bg ->
            context.fill(
                x.toInt(),
                y.toInt(),
                (x + bgWidth).toInt(),
                (y + bgHeight - 2).toInt(),
                bg.copy(
                    a = (bg.a * props.opacity).coerceIn(0f, 255f).toInt()
                ).toArgb()
            )
        }

        val textStartX = x + padding.left
        val textStartY = y + padding.top

        context.pose().pushMatrix()
        context.pose().translate(textStartX.toFloat(), textStartY.toFloat())
        context.pose().scale(scaleX.toFloat(), scaleY.toFloat())

        val color =
            props.color.copy(
                a = (props.color.a * props.opacity).coerceIn(0f, 255f).toInt()
            ).toArgb()

        context.drawString(
            renderer,
            mcText,
            0,
            0,
            0xFFFFFFFF.toInt(),
            props.shadow
        )

        context.pose().popMatrix()
    }
}
