package computer.obscure.piku.minestom.scripting

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import computer.obscure.piku.minestom.scripting.api.LuaPlayer

class LuaServerEventListener : TwineNative() {
    lateinit var bus: ServerEventBus

    @TwineFunction
    fun listen(id: String, callback: (Any?) -> Unit) {
        bus.listen(id) { data ->
            callback.invoke(data)
        }
    }

    @TwineFunction
    fun send(player: LuaPlayer, id: String, data: Any?) {
        bus.send(player, id, data)
    }
}