package me.znotchill.piku.common.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import me.znotchill.piku.common.scripting.base.EventBus
import org.luaj.vm2.LuaValue

class LuaEventListener : TwineNative() {
    lateinit var bus: EventBus

    @TwineNativeFunction
    fun listen(id: String, callback: Function1<LuaValue, Unit>) {
        bus.listen(id) { data ->
            callback.invoke(data)
        }
    }

    @TwineNativeFunction
    fun fire(id: String, data: LuaValue) {
        bus.fire(id, data)
    }

}