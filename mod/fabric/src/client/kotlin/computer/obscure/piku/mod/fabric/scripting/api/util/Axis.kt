package computer.obscure.piku.mod.fabric.scripting.api.util

import computer.obscure.piku.mod.fabric.ui.CrossAxisAlignment
import computer.obscure.piku.mod.fabric.ui.MainAxisAlignment

object Axis {
    fun parseMainAxis(value: String) = when (value) {
        "start" -> MainAxisAlignment.Start
        "center" -> MainAxisAlignment.Center
        "end" -> MainAxisAlignment.End
        "space_between", "spaceBetween" -> MainAxisAlignment.SpaceBetween
        "space_around", "spaceAround" -> MainAxisAlignment.SpaceAround
        else -> MainAxisAlignment.Start
    }

    fun parseCrossAxis(value: String) = when (value) {
        "start" -> CrossAxisAlignment.Start
        "center" -> CrossAxisAlignment.Center
        "end" -> CrossAxisAlignment.End
        else -> CrossAxisAlignment.Start
    }
}