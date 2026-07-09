package computer.obscure.piku.mod.fabric.compat

import net.fabricmc.loader.api.FabricLoader

object ModCompat {
    val controlifyLoaded = isLoaded("controlify")

    fun isLoaded(mod: String) = FabricLoader.getInstance().isModLoaded(mod)
}