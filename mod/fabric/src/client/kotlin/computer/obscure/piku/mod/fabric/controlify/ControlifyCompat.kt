package computer.obscure.piku.mod.fabric.controlify

import computer.obscure.piku.mod.fabric.compat.ModCompat
import dev.isxander.controlify.api.bind.InputBindingSupplier

object ControlifyCompat {
    private val available by lazy {
        ModCompat.controlifyLoaded
    }

    private val bindingsClass by lazy {
        if (!available) null
        else Class.forName("dev.isxander.controlify.bindings.ControlifyBindings")
    }

    private val bindingCache by lazy {
        ControllerBind.entries.associateWith { bind ->
            getBinding(bind)
        }
    }

    fun getBinding(bind: ControllerBind): InputBindingSupplier? {
        return try {
            bindingsClass
                ?.getField(bind.name)
                ?.get(null) as? InputBindingSupplier
        } catch (_: Exception) {
            null
        }
    }

    fun getBind(binding: InputBindingSupplier): ControllerBind? {
        return bindingCache.entries
            .firstOrNull { it.value === binding }
            ?.key
    }
}