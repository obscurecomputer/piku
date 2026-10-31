package computer.obscure.piku.core.scripting.base

interface EventBus {
    fun listen(eventId: String, callback: (Any?) -> Unit)
    fun fire(eventId: String, data: Any?)
}