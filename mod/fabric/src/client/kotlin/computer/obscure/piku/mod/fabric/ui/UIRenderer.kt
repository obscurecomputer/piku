package computer.obscure.piku.mod.fabric.ui

import com.mojang.blaze3d.platform.NativeImage
import computer.obscure.piku.core.service.PikuService
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaEasingInstance
import computer.obscure.piku.mod.fabric.ui.components.*
import computer.obscure.twine.LuaCallback
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier

object UIRenderer : PikuService {
    val instance = Minecraft.getInstance()
    val roots = mutableListOf<UINode>()
    val registeredEasings = mutableMapOf<String, LuaCallback>()

    override fun shutdown() {
        roots.clear()
    }

    fun registerEasing(easing: LuaEasingInstance) {
        registeredEasings[easing.id] = easing.function
    }

    fun addRoot(node: UINode) {
        roots.add(node)
    }

    fun removeRoot(node: UINode) {
        roots.remove(node)
    }

    fun clearRoots() {
        roots.clear()
    }

    fun findByName(name: String): UINode? {
        return roots.firstNotNullOfOrNull { searchTree(it, name) }
    }

    fun findById(id: String): UINode? {
        return roots.firstNotNullOfOrNull { searchTreeById(it, id) }
    }

    private fun searchTree(node: UINode, name: String): UINode? {
        if (node.name == name) return node
        return node.children.firstNotNullOfOrNull { searchTree(it, name) }
    }

    private fun searchTreeById(node: UINode, id: String): UINode? {
        if (node.id == id) return node
        return node.children.firstNotNullOfOrNull { searchTreeById(it, id) }
    }

    fun render(graphics: GuiGraphics) {
        val screenW = instance.window.guiScaledWidth
        val screenH = instance.window.guiScaledHeight
        val scale = instance.window.guiScale.toFloat()
        val measureCtx = MeasureContext(instance.font, screenW.toFloat(), screenH.toFloat(), scale)
        val layoutCtx = LayoutContext(x = 0f, y = 0f, parentWidth = screenW.toFloat(), parentHeight = screenH.toFloat())

        roots.forEach { root ->
            root.measureSelf(measureCtx)
            root.layoutSelf(layoutCtx)
            root.drawSelf(graphics, measureCtx)
        }
    }

    fun getTexture(path: String): DynamicTexture? {
        val id = getIdentifier(path) ?: return null
        return try {
            val resourceManager = instance.resourceManager
            val resource = resourceManager.getResource(id).orElse(null) ?: return null
            val image = NativeImage.read(resource.open())
            DynamicTexture({ "piku_texture_$path" }, image)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getIdentifier(path: String): Identifier? {
        return try {
            if (':' in path) Identifier.parse(path)
            else Identifier.fromNamespaceAndPath("minecraft", path)
        } catch (e: Exception) {
            null
        }
    }
}