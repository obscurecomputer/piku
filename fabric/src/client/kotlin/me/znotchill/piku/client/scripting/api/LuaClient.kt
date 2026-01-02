package me.znotchill.piku.client.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import me.znotchill.piku.client.utils.parseMini
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.Perspective

class LuaClient : TwineNative("client") {
    val instance: MinecraftClient = MinecraftClient.getInstance()!!

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