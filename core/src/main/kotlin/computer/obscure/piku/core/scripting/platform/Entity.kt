package computer.obscure.piku.core.scripting.platform

import computer.obscure.piku.core.classes.Pos

interface Entity<EntityType> {
    val type: EntityType
    var position: Pos
}