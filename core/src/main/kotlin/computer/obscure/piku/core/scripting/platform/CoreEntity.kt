package computer.obscure.piku.core.scripting.platform

import computer.obscure.piku.core.classes.Pos
import java.util.UUID

interface CoreEntity<T> : WireRef {
    val uuid: UUID

    override val refType: String
        get() = "entity"

    override val refId: String
        get() = uuid.toString()

    val type: T
    var position: Pos
}