package computer.obscure.piku.core.scripting.platform

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

open class Platform {

    private val factories = ConcurrentHashMap<String, RefFactory>()

    fun register(factory: RefFactory) {
        factories[factory.kind] = factory
    }

    fun resolve(kind: String, uuid: UUID): Any? {
        return factories[kind]?.resolve(uuid)
    }

    companion object {
        @Volatile
        var current: Platform? = null
    }
}