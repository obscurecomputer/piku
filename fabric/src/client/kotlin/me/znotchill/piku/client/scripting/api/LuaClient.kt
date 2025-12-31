package me.znotchill.piku.client.scripting.api

import me.znotchill.piku.client.utils.parseMini
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.Perspective

class LuaClient {
    val instance: MinecraftClient = MinecraftClient.getInstance()!!

    fun sendActionbar(message: String) {
        instance.player?.sendMessage(parseMini(message), true)
    }

    fun send(message: String) {
        instance.player?.sendMessage(parseMini(message), false)
    }

    fun setPerspective(perspective: String) {
        val enum = Perspective.valueOf(perspective)

        instance.options.perspective = enum
    }
}