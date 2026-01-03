package me.znotchill.piku.minestom.scripting

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import me.znotchill.piku.minestom.scripting.api.LuaPlayer
import org.luaj.vm2.LuaValue

class LuaServerEventListener : TwineNative() {
    lateinit var bus: ServerEventBus

    @TwineNativeFunction
    fun listen(id: String, callback: Function1<LuaValue, Unit>) {
        bus.listen(id) { data ->
            callback.invoke(data)
        }
    }

    @TwineNativeFunction
    fun send(player: LuaPlayer, id: String, data: LuaValue) {
        bus.send(player, id, data)
    }
}