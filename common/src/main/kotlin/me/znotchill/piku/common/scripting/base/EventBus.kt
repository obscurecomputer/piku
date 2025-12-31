package me.znotchill.piku.common.scripting.base

import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

interface EventBus {
    fun listen(eventId: String, callback: (LuaValue) -> Unit)
    fun fire(eventId: String, data: LuaValue)
}

fun EventBus.registerListen(globals: Globals) {
    println("registered listener")
    globals["listen"] = object : TwoArgFunction() {
        override fun call(eventArg: LuaValue, fnArg: LuaValue): LuaValue {
            val eventId = eventArg.checkjstring()
            val fn = fnArg.checkfunction()

            listen(eventId) { data ->
                fn.call(data)
            }

            return NIL
        }
    }
}