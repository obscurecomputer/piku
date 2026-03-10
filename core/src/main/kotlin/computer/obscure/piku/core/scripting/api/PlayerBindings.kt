package computer.obscure.piku.core.scripting.api

import computer.obscure.piku.core.scripting.base.Player
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction

object PlayerBindings {

    fun createMetatable(): LuaTable {
        val mt = LuaTable()

        mt["__index"] = object : TwoArgFunction() {
            override fun call(self: LuaValue, key: LuaValue): LuaValue {
                val player = self.touserdata() as? Player
                    ?: return NIL

                return when (key.checkjstring()) {
                    "uuid" -> valueOf(player.uuid)
                    "username", "name" -> valueOf(player.username)

                    "send" -> object : OneArgFunction() {
                        override fun call(arg: LuaValue): LuaValue {
                            player.send(arg.checkjstring())
                            return NIL
                        }
                    }

                    else -> NIL
                }
            }
        }

        return mt
    }
}