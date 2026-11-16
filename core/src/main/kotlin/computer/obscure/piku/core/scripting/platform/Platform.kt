package computer.obscure.piku.core.scripting.platform

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

abstract class Platform {
    abstract fun entityFromDto(dto: CoreEntityDto): CoreEntity?
    abstract fun playerFromDto(dto: CorePlayerDto): CorePlayer?

    companion object {
        lateinit var current: Platform
    }
}