package computer.obscure.piku.client.ui.events

import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.events.UIEvent

interface UIEventHandler<E : UIEvent> {
    fun handle(event: E, component: Component, context: UIEventContext)
}