package computer.obscure.piku.core.scripting.base

interface EventBus {
    fun listen(eventId: String, callback: (Map<String, Any?>) -> Unit)
    fun fire(eventId: String, data: Map<String, Any?>)
}