package computer.obscure.piku.core.scripting.platform

import java.util.UUID

interface RefFactory {
    val kind: String

    fun resolve(uuid: UUID): Any?
}