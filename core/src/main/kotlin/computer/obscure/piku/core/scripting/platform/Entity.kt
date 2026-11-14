package computer.obscure.piku.core.scripting.platform

import computer.obscure.piku.core.classes.Pos

interface Entity<T> {
    val type: T
    var position: Pos
}