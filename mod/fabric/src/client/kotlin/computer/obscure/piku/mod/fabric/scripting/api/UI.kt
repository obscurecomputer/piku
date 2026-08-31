package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import computer.obscure.piku.mod.fabric.ui.components.UINode
import computer.obscure.piku.mod.fabric.ui.menu.UIMenu
import computer.obscure.piku.mod.fabric.utils.toNativeComponent
import net.kyori.adventure.text.Component

fun ui() = UI
fun ui(block: UI.() -> Unit) = UI.apply(block)

fun menu(title: Component = Component.empty()) = Menu(title)
fun menu(block: Menu.() -> Unit) = Menu(Component.empty()).apply(block)

object UI : UIBuilder {
    override fun <T : UINode> add(node: T): T {
        UIRenderer.addRoot(node)
        return node
    }
}

class Menu(
    val title: Component = Component.empty()
) : UIBuilder {
    val screen = UIMenu(title.toNativeComponent())

    override fun <T : UINode> find(name: String, type: Class<T>): T? {
        return screen.roots.firstNotNullOfOrNull { searchTree(it, name) }
            ?.let { type.cast(it) }
    }

    override fun <T : UINode> findById(id: String, type: Class<T>): T? {
        return null
    }

    override fun <T : UINode> add(node: T): T {
        screen.addRoot(node)
        return node
    }

    fun open() = apply {
        PikuClient.minecraft.gui.setScreen(screen)
    }
}
