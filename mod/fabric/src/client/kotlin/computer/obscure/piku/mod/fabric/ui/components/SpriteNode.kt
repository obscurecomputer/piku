package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.mod.fabric.ui.classes.Dimension
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier

class SpriteNode() : UINode() {
    var texturePath: String = ""
        set(value) {
            if (field != value) {
                field = value
                resolved = false
                resolvedTexture = null
                textureId = null
            }
        }

    constructor(texturePath: String) : this() {
        this.texturePath = texturePath
    }

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
            val path = texturePath
                .replace(":", "_")
                .replace("/", "_")
            val id = Identifier.fromNamespaceAndPath(
                "piku",
                "sprite_${path}")
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
            w, h,
            color.withOpacity(computedOpacity).argb
        )
    }
}