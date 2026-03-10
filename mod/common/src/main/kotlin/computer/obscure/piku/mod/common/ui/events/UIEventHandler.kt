package computer.obscure.piku.mod.common.ui.events

import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.events.UIEvent

interface UIEventHandler<E : UIEvent> {
    fun handle(event: E, component: Component, context: UIEventContext)
}