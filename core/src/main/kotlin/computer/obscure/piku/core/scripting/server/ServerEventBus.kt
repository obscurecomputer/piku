package computer.obscure.piku.core.scripting.server

import computer.obscure.piku.core.scripting.base.EventBus

interface ServerEventBus<P> : EventBus {
    fun send(player: P, eventId: String, data: Any?)
}