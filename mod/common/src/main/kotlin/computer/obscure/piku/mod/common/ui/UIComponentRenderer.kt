package computer.obscure.piku.mod.common.ui

import computer.obscure.piku.core.ui.components.Component
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics

/**
 * Renders [Component]s to their corresponding [computer.obscure.piku.mod.common.ui.components.UIComponent] implementations
 * to be rendered on the screen.
 */
class UIComponentRenderer(
    private val handlers: Map<Class<out Component>, computer.obscure.piku.mod.common.ui.components.UIComponent<out Component>>
) {
    fun draw(component: Component, context: GuiGraphics, instance: Minecraft) {
        val handler = handlers[component::class.java]
        if (handler != null) {
            @Suppress("UNCHECKED_CAST")
            (handler as computer.obscure.piku.mod.common.ui.components.UIComponent<Component>).draw(component, context, instance)
        } else {
//            pikuClient.LOGGER.error("UI Component Renderer handler not found for ${component.compType}")
        }
    }
}