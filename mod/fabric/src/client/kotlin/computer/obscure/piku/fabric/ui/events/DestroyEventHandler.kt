package computer.obscure.piku.fabric.ui.events

import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.events.DestroyEvent

class DestroyEventHandler : UIEventHandler<DestroyEvent> {
    override fun handle(event: DestroyEvent, component: Component, context: UIEventContext) {
        context.currentWindow()?.components?.remove(event.targetId)
    }
}