package computer.obscure.piku.minestom.scripting.api.instance

import computer.obscure.twine.LuaCallback
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.LightingChunk
import net.minestom.server.instance.SharedInstance
import net.minestom.server.utils.chunk.ChunkSupplier

class LuaInstance(
    val instance: InstanceContainer
) : TwineNative() {
    lateinit var sharedInstance: SharedInstance

    init {
        instance.chunkSupplier = ChunkSupplier { i, x, z -> LightingChunk(i, x, z) }
    }

    @TwineFunction
    fun shared(value: Boolean = true) = this.apply {
        sharedInstance = MinecraftServer.getInstanceManager().createSharedInstance(instance)
    }

    @TwineFunction
    fun generator(callback: LuaCallback) {
        instance.setGenerator { unit ->
            try {
                callback.invoke(LuaInstanceUnit(unit))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}