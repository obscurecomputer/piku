package computer.obscure.piku.mod.common.scripting.api.util

import java.util.Optional

fun Any.unwrap(): Any? = when (this) {
    is Optional<*> -> orElse(null)
    else -> this
}