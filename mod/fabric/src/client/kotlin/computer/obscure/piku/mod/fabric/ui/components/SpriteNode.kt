package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.mod.fabric.ui.Dimension
import computer.obscure.piku.mod.fabric.ui.MeasureContext
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier

class SpriteNode(var texturePath: String) : UINode() {
    private var resolvedTexture: DynamicTexture? = null
    private var textureId: Identifier? = null
    private var resolved = false

    var fillScreen: Boolean = false
        set(value) {
            field = value
            if (value) {
                width = Dimension.Fill
                height = Dimension.Fill
            }
        }

    private fun resolveOnce() {
        if (resolved) return
        resolved = true
        resolvedTexture = UIRenderer.getTexture(texturePath)
        resolvedTexture?.let {
            val id = Identifier.fromNamespaceAndPath("piku", "sprite_${texturePath.replace(":", "_").replace("/", "_")}")
            Minecraft.getInstance().textureManager.register(id, it)
            textureId = id
            val img = it.pixels
            if (img != null && width == Dimension.Wrap && height == Dimension.Wrap) {
                measuredWidth = img.width.toFloat()
                measuredHeight = img.height.toFloat()
            }
        }
    }

    override fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        resolveOnce()
        val img = resolvedTexture?.pixels
        return if (img != null) img.width.toFloat() to img.height.toFloat()
        else 16f to 16f // fallback size
    }

    override fun drawContent(graphics: GuiGraphics, ctx: MeasureContext) {
        resolveOnce()
        val id = textureId ?: return
        val w = measuredWidth.toInt()
        val h = measuredHeight.toInt()
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            id,
            layoutX.toInt(),
            layoutY.toInt(),
            0f, 0f,
            w, h,
            w, h
        )
    }
}