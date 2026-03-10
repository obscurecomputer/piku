package computer.obscure.piku.mod.common.ui

import computer.obscure.piku.mod.common.ui.events.UIEventContext
import computer.obscure.piku.mod.common.ui.events.UIEventHandler
import computer.obscure.piku.core.ui.events.UIEvent

/**
 * Dispatches [UIEvent]s to their corresponding [computer.obscure.piku.mod.common.ui.events.UIEventHandler] implementations
 * to be animated and key-framed in the future.
 */
class UIEventDispatcher(
    private val handlers: Map<Class<out UIEvent>, computer.obscure.piku.mod.common.ui.events.UIEventHandler<out UIEvent>>,
    private val context: computer.obscure.piku.mod.common.ui.events.UIEventContext
) {
    /**
     * Dispatches the given [UIEvent] to the appropriate [computer.obscure.piku.mod.common.ui.events.UIEventHandler].
     *
     * If the event's target component cannot be found in [computer.obscure.piku.mod.common.ui.events.UIEventContext.currentWindow],
     * the event is ignored.
     *
     * If no handler is registered for the type, a warning is logged instead
     * of throwing an exception.
     */
    fun applyEvent(event: UIEvent) {
        val comp = context.currentWindow()?.getComponentByIdDeep(event.targetId) ?: return

        val handler = handlers[event::class.java]
        if (handler != null) {
            @Suppress("UNCHECKED_CAST")
            (handler as computer.obscure.piku.mod.common.ui.events.UIEventHandler<UIEvent>).handle(event, comp, context)
        } else {
//            pikuClient.LOGGER.error("UI Component Event handler not found for: $event")
        }
    }
}