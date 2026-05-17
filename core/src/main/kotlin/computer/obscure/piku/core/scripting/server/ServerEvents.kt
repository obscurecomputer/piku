package computer.obscure.piku.core.scripting.server

open class ServerEvents<P> : ServerEventBus<P> {
    private val listeners =
        mutableMapOf<String, MutableList<(Map<String, Any?>, P) -> Unit>>()

    fun fire(eventId: String, data: Map<String, Any?>, player: P) {
        listeners[eventId]?.forEach { it(data, player) }
    }

    override fun listen(
        eventId: String,
        callback: (Map<String, Any?>) -> Unit
    ) {
        listeners.computeIfAbsent(eventId) { mutableListOf() }
            .add { data, _ -> callback(data) }
    }

    override fun fire(eventId: String, data: Map<String, Any?>) {
        // platform specific override later
    }

    fun listen(
        eventId: String,
        callback: (Map<String, Any?>, P) -> Unit
    ) {
        listeners.computeIfAbsent(eventId) { mutableListOf() }
            .add(callback)
    }

    override fun send(player: P, eventId: String, data: Any?) {
        // platform specific override later
    }
}