package computer.obscure.piku.mod.fabric.scripting

import computer.obscure.piku.core.scripting.base.EventBus

interface ClientEventBus : EventBus {
    fun send(eventId: String, data: Map<String, Any?>)
}