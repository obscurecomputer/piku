package computer.obscure.piku.client.ui

import computer.obscure.piku.client.ui.components.UIComponent
import computer.obscure.piku.common.ui.components.Component
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * Renders [Component]s to their corresponding [UIComponent] implementations
 * to be rendered on the screen.
 */
class UIComponentRenderer(
    private val handlers: Map<Class<out Component>, UIComponent<out Component>>
) {
    fun draw(component: Component, context: DrawContext, instance: MinecraftClient) {
        val handler = handlers[component::class.java]
        if (handler != null) {
            @Suppress("UNCHECKED_CAST")
            (handler as UIComponent<Component>).draw(component, context, instance)
        } else {
//            pikuClient.LOGGER.error("UI Component Renderer handler not found for ${component.compType}")
        }
    }
}