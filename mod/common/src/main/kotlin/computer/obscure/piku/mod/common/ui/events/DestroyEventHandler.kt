package computer.obscure.piku.mod.common.ui.events

import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.events.DestroyEvent

class DestroyEventHandler : UIEventHandler<DestroyEvent> {
    override fun handle(event: DestroyEvent, component: Component, context: UIEventContext) {
        context.currentWindow()?.components?.remove(event.targetId)
    }
}