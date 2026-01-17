package computer.obscure.piku.client.scripting.api.util

import java.util.Optional

fun Any.unwrap(): Any? = when (this) {
    is Optional<*> -> orElse(null)
    else -> this
}