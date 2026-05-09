package computer.obscure.piku.mod.fabric.ui

import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.mod.fabric.ui.components.UIComponent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics

/**
 * Renders [Component]s to their corresponding [UIComponent] implementations
 * to be rendered on the screen.
 */
class UIComponentRenderer(
    private val handlers: Map<Class<out Component>, UIComponent<out Component>>
) {
    fun draw(component: Component, context: GuiGraphics, instance: Minecraft) {
        val handler = handlers[component::class.java]
        if (handler != null) {
            @Suppress("UNCHECKED_CAST")
            (handler as UIComponent<Component>).draw(component, context, instance)
        } else {
//            pikuClient.LOGGER.error("UI Component Renderer handler not found for ${component.compType}")
        }
    }
}