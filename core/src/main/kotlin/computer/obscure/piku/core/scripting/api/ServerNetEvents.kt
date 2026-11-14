package computer.obscure.piku.core.scripting.api

import computer.obscure.piku.core.scripting.events.Connection
import computer.obscure.piku.core.scripting.events.NetBus
import computer.obscure.twine.LuaCallback
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction

class NetEvents<P>(
    private val bus: NetBus<P>,
    private val wrap: (P) -> TwineNative?,
    private val unwrap: (TwineNative?) -> P
) : TwineNative("net") {

    private val connections = mutableListOf<Connection>()

    @TwineFunction
    fun listen(channel: String, callback: LuaCallback): Connection {
        val connection = bus.listen(channel) { data, peer ->
            callback.invoke(data, wrap(peer))
        }

        connections += connection
        return connection
    }

    @TwineFunction
    fun emit(channel: String, data: Any?, player: TwineNative? = null) {
        bus.emit(channel, data, unwrap(player))
    }

    fun disconnectAll() {
        connections.forEach { it.disconnect() }
        connections.clear()
    }
}