package computer.obscure.piku.core.scripting.events

import computer.obscure.twine.TwineLogger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class EventBus {
    private class Listener(
        val once: Boolean,
        val handler: (Any?) -> Unit
    )

    private val listeners = ConcurrentHashMap<String, CopyOnWriteArrayList<Listener>>()

    fun listen(
        channel: String,
        once: Boolean = false,
        handler: (data: Any?) -> Unit
    ): Connection {
        val listener = Listener(once, handler)

        listeners
            .computeIfAbsent(channel) { CopyOnWriteArrayList<Listener>() }
            .add(listener)

        return Connection {
            listeners[channel]?.remove(listener)
        }
    }

    fun emit(channel: String, data: Any? = null) {
        val list = listeners[channel] ?: return

        for (listener in list) {
            if (listener.once && !list.remove(listener)) {
                continue
            }

            try {
                listener.handler(data)
            } catch (e: Exception) {
                TwineLogger.error("Listener on '$channel' failed: ${e.message}")
            }
        }
    }
}