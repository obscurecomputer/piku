package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.ui.components.Text
import computer.obscure.piku.mod.fabric.ui.TextInterpolator
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
        val maxWidth = props.maxWidth ?: Int.MAX_VALUE

        val lines = renderer.split(mcText, maxWidth)
        val totalHeight = renderer.lineHeight * lines.size

        val scaleX = props.textScale.x
        val scaleY = props.textScale.y

        val padding = props.padding

        val contentWidth: Float = if (props.maxWidth != null) {
            maxWidth.toFloat()
        } else if (lines.isEmpty()) {
            0f
        } else {
            lines.maxOf { renderer.width(it).toFloat() } * scaleX.toFloat()
        }

        val bgWidth = contentWidth + padding.left + padding.right
        val bgHeight = totalHeight * scaleY + padding.top + padding.bottom

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

        lines.forEachIndexed { i, line ->
            context.drawString(
                renderer,
                line,
                0,
                i * renderer.lineHeight,
                color,
                props.shadow
            )
        }

        context.pose().popMatrix()
    }
}
