package computer.obscure.piku.core.scripting.events

import computer.obscure.twine.TwineLogger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class NetBus<P>(
    private val send: (channel: String, data: Any?, peer: P) -> Unit
) {
    private class Listener<P>(
        val once: Boolean,
        val handler: (Any?, P) -> Unit
    )

    private val listeners = ConcurrentHashMap<String, CopyOnWriteArrayList<Listener<P>>>()

    fun listen(
        channel: String,
        once: Boolean = false,
        handler: (data: Any?, peer: P) -> Unit
    ): Connection {
        val listener = Listener(once, handler)

        listeners
            .computeIfAbsent(channel) {
                CopyOnWriteArrayList<Listener<P>>()
            }
            .add(listener)

        return Connection {
            listeners[channel]?.remove(listener)
        }
    }

    /** incoming from network */
    fun receive(channel: String, data: Any?, peer: P) {
        val list = listeners[channel] ?: return

        for (listener in list) {
            if (listener.once && !list.remove(listener)) {
                continue
            }

            try {
                listener.handler(data, peer)
            } catch (e: Exception) {
                TwineLogger.error("Listener on '$channel' failed: ${e.message}")
            }
        }
    }

    /** sends over network */
    fun emit(channel: String, data: Any?, peer: P) {
        send(channel, data, peer)
    }
}