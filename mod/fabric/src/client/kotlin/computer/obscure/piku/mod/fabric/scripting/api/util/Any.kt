package computer.obscure.piku.mod.fabric.scripting.api.util

import java.util.Optional

fun Any.unwrap(): Any? = when (this) {
    is Optional<*> -> orElse(null)
    else -> this
}