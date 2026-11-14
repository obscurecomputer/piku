package computer.obscure.piku.core.scripting.api

import computer.obscure.piku.core.scripting.events.Connection
import computer.obscure.piku.core.scripting.events.EventBus
import computer.obscure.twine.LuaCallback
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction

class LocalScriptEvents(
    private val bus: EventBus
) : TwineNative("events") {

    private val connections = mutableListOf<Connection>()

    @TwineFunction
    fun listen(channel: String, callback: LuaCallback): Connection {
        val connection = bus.listen(channel) { data ->
            callback.invoke(data)
        }

        connections += connection
        return connection
    }

    @TwineFunction
    fun emit(channel: String, data: Any?) {
        bus.emit(channel, data)
    }

    fun disconnectAll() {
        connections.forEach { it.disconnect() }
        connections.clear()
    }
}