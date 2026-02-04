package computer.obscure.piku.mod.common.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.piku.mod.common.scripting.ClientEventBus
import org.luaj.vm2.LuaValue

class LuaClientEventListener : TwineNative() {
    lateinit var bus: ClientEventBus

    @TwineNativeFunction
    fun listen(id: String, callback: Function1<LuaValue, Unit>) {
        bus.listen(id) { data ->
            callback.invoke(data)
        }
    }

    @TwineNativeFunction
    fun send(id: String, data: LuaValue) {
        bus.send(id, data)
    }
}