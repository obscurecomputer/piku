package computer.obscure.piku.mod.fabric.ui

import com.mojang.blaze3d.platform.NativeImage
import computer.obscure.piku.core.service.PikuService
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaEasingInstance
import computer.obscure.piku.mod.fabric.ui.classes.context.LayoutContext
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import computer.obscure.piku.mod.fabric.ui.components.*
import computer.obscure.twine.LuaCallback
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier

object UIRenderer : PikuService {
    val instance = Minecraft.getInstance()
    val roots = mutableListOf<UINode>()
    val registeredEasings = mutableMapOf<String, LuaCallback>()

    private val allNodes = LinkedHashSet<UINode>()
    private val nodesByName = mutableMapOf<String, UINode>()
    private val nodesById = mutableMapOf<String, UINode>()
    val nodesByType = mutableMapOf<Class<out UINode>, LinkedHashSet<UINode>>()

    override fun shutdown() {
        clearRoots()
    }

    fun registerEasing(easing: LuaEasingInstance) {
        registeredEasings[easing.id] = easing.function
    }

    fun addRoot(node: UINode) {
        roots.add(node)
        indexTree(node)
    }

    fun removeRoot(node: UINode) {
        roots.remove(node)
        deindexTree(node)
    }

    fun registerNode(node: UINode) {
        indexTree(node)
    }

    fun clearRoots() {
        roots.clear()
        allNodes.clear()
        nodesByName.clear()
        nodesById.clear()
        nodesByType.clear()
    }

    private fun indexTree(node: UINode) {
        allNodes.add(node)
        node.name?.let { nodesByName[it] = node }
        nodesById[node.id] = node

        var cls: Class<*>? = node::class.java
        while (cls != null && UINode::class.java.isAssignableFrom(cls)) {
            @Suppress("UNCHECKED_CAST")
            nodesByType.getOrPut(cls as Class<out UINode>) {
                LinkedHashSet()
            }.add(node)
            cls = cls.superclass
        }

        node.children.forEach { indexTree(it) }
    }

    fun deindexTree(node: UINode) {
        allNodes.remove(node)
        node.name?.let { nodesByName.remove(it, node) }
        nodesById.remove(node.id, node)

        var cls: Class<*>? = node::class.java
        while (cls != null && UINode::class.java.isAssignableFrom(cls)) {
            @Suppress("UNCHECKED_CAST")
            nodesByType[cls as Class<out UINode>]?.remove(node)
            cls = cls.superclass
        }

        node.children.forEach { deindexTree(it) }
    }

    fun findByName(name: String): UINode? = nodesByName[name]
    fun findById(id: String): UINode? = nodesById[id]

    inline fun <reified T : UINode> findAllOfType(): List<T> =
        (nodesByType[T::class.java] as? Set<T>)?.toList() ?: emptyList()

    fun findAllByTypeName(type: String): List<UINode> =
        nodesByType.entries.firstOrNull { it.key.simpleName == type }?.value?.toList() ?: emptyList()

    fun getAllNodes(): List<UINode> = allNodes.toList()

    fun render(graphics: GuiGraphicsExtractor) {
        val screenW = instance.window.guiScaledWidth
        val screenH = instance.window.guiScaledHeight
        val scale = instance.window.guiScale.toFloat()
        val measureCtx = MeasureContext(instance.font, screenW.toFloat(), screenH.toFloat(), scale)
        val layoutCtx = LayoutContext(
            x = 0f,
            y = 0f,
            parentWidth = screenW.toFloat(),
            parentHeight = screenH.toFloat()
        )
        roots.forEach { root ->
            root.measureSelf(measureCtx)
            root.layoutSelf(layoutCtx)
        }
        roots.forEach { root ->
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