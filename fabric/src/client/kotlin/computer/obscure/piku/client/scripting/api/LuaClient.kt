package computer.obscure.piku.client.scripting.api

import computer.obscure.piku.client.Client
import computer.obscure.piku.client.utils.parseMini
import computer.obscure.piku.common.scripting.api.LuaVec2Instance
import computer.obscure.piku.common.scripting.api.LuaVec3Instance
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.Perspective
import net.minecraft.item.ItemStack

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

    @TwineNativeProperty
    val headRot: LuaVec3Instance
        get() {
            val p = instance.player
                ?: return LuaVec3Instance(0.0, 0.0, 0.0)

            return LuaVec3Instance(p.pitch.toDouble(), p.yaw.toDouble(), 0.0)
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

    @TwineNativeProperty
    var hideHotbar: Boolean
        get() = Client.hideHotbar
        set(value) { Client.hideHotbar = value }

    @TwineNativeProperty
    var hideArm: Boolean
        get() = Client.hideArm
        set(value) { Client.hideArm = value }

    @TwineNativeProperty
    var hideHUD: Boolean
        get() = Client.hideHUD
        set(value) { Client.hideHUD = value }

    @TwineNativeProperty
    var selectedSlot: Int
        get() = instance.player?.inventory?.selectedSlot ?: 0
        set(value) {
            instance.player?.inventory?.selectedSlot = value.coerceIn(0, 8)
        }

    @TwineNativeFunction
    fun getItem(slot: Int): LuaItem? {
        val player = instance.player ?: return null
        val inv = player.inventory

        if (slot !in 0 until inv.size()) return null

        val stack = inv.getStack(slot)
        if (stack.isEmpty) return null

        return LuaItem().setStack(stack)
    }

    @TwineNativeFunction
    fun clearSlot(slot: Int) {
        val player = instance.player ?: return
        val inv = player.inventory

        if (slot !in 0 until inv.size()) return

        inv.setStack(slot, ItemStack.EMPTY)
    }

    @TwineNativeProperty
    val heldItem: LuaItem?
        get() {
            val player = instance.player ?: return null
            val stack = player.mainHandStack
            return if (stack.isEmpty) null
            else LuaItem().setStack(stack)
        }

    @TwineNativeProperty
    val windowSize: LuaVec2Instance
        get() {
            val x = instance.window.width.toDouble()
            val y = instance.window.height.toDouble()
            return LuaVec2Instance(x, y)
        }
}