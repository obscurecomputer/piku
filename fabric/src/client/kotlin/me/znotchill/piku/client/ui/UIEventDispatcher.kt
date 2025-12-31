package me.znotchill.piku.client.ui

import me.znotchill.piku.client.ui.events.UIEventContext
import me.znotchill.piku.client.ui.events.UIEventHandler
import me.znotchill.piku.common.ui.events.UIEvent

/**
 * Dispatches [UIEvent]s to their corresponding [UIEventHandler] implementations
 * to be animated and key-framed in the future.
 */
class UIEventDispatcher(
    private val handlers: Map<Class<out UIEvent>, UIEventHandler<out UIEvent>>,
    private val context: UIEventContext
) {
    /**
     * Dispatches the given [UIEvent] to the appropriate [UIEventHandler].
     *
     * If the event's target component cannot be found in [UIEventContext.currentWindow],
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
            (handler as UIEventHandler<UIEvent>).handle(event, comp, context)
        } else {
//            pikuClient.LOGGER.error("UI Component Event handler not found for: $event")
        }
    }
}