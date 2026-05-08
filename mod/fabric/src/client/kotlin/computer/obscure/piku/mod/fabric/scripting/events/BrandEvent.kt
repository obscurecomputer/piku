package computer.obscure.piku.mod.fabric.scripting.events

import computer.obscure.piku.core.scripting.base.LuaEvent
import computer.obscure.piku.mod.fabric.PikuClient

object BrandEvent : LuaEvent() {
    override val id = "piku.brand"
    override val onClientReceive: (data: Any) -> Unit
        get() = {
            PikuClient.engine!!.events.send(
                id,
                mapOf(
                    "brand" to "piku"
                )
            )
        }
}