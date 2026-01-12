package computer.obscure.piku.client.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.piku.client.utils.parseMini
import computer.obscure.piku.common.scripting.api.LuaVec3
import computer.obscure.piku.common.scripting.api.LuaVec3Instance
import computer.obscure.twine.annotations.TwineNativeProperty
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.Perspective

class LuaClient : TwineNative("client") {
    val instance: MinecraftClient = MinecraftClient.getInstance()!!

    @TwineNativeProperty
    val pos: LuaVec3Instance
        get() {
            val p = instance.player
                ?: return LuaVec3Instance(0.0, 0.0, 0.0)

            return LuaVec3Instance(p.x, p.y, p.z)
        }

    @TwineNativeProperty
    val headPos: LuaVec3Instance
        get() {
            val p = instance.player
                ?: return LuaVec3Instance(0.0, 0.0, 0.0)

            val v = p.getCameraPosVec(1.0f)
            return LuaVec3Instance(v.x, v.y, v.z)
        }

    @TwineNativeFunction
    fun sendActionbar(message: String) {
        instance.player?.sendMessage(parseMini(message), true)
    }

    @TwineNativeFunction
    fun send(message: String) {
        instance.player?.sendMessage(parseMini(message), false)
    }

    @TwineNativeFunction
    fun setPerspective(perspective: String) {
        val enum = Perspective.valueOf(perspective)

        instance.options.perspective = enum
    }
}