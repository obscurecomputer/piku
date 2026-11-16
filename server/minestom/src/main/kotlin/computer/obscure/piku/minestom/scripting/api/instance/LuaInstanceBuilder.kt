package computer.obscure.piku.minestom.scripting.api.instance

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import net.minestom.server.MinecraftServer

class LuaInstanceBuilder : TwineNative("instance") {
    @TwineFunction
    fun new(): LuaInstance {
        val instance = MinecraftServer.getInstanceManager().createInstanceContainer()
        return LuaInstance(instance)
    }
}