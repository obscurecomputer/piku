package computer.obscure.piku.core.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

fun Component.getDecoration(decoration: TextDecoration): Boolean
    = this.decoration(decoration) == TextDecoration.State.TRUE