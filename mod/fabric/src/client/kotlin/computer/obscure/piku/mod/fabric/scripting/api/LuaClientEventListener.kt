package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import computer.obscure.piku.mod.fabric.scripting.ClientEventBus
import computer.obscure.twine.LuaCallback

class LuaClientEventListener : TwineNative("events") {
    lateinit var bus: ClientEventBus

    @TwineFunction
    fun listen(id: String, callback: LuaCallback) {
        bus.listen(id) { data ->
            callback.invoke(data)
        }
    }

    @TwineFunction
    fun send(id: String, data: Any) {
        bus.send(id, mapOf("data" to data))
    }
}