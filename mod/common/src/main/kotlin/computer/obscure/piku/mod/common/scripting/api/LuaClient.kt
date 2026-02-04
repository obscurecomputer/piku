package computer.obscure.piku.mod.common.scripting.api

import computer.obscure.piku.mod.common.Client
import computer.obscure.piku.core.scripting.api.LuaTextInstance
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.piku.mod.common.utils.parseMini
import computer.obscure.twine.TwineLogger
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.annotations.TwineOverload
import computer.obscure.twine.nativex.TwineNative
import net.minecraft.client.CameraType
import net.minecraft.client.Minecraft
import net.minecraft.world.item.ItemStack

class LuaClient : TwineNative("client") {
    val instance: Minecraft = Minecraft.getInstance()!!

    @TwineNativeFunction
    fun debug(value: Boolean) {
        TwineLogger.level = if (value) TwineLogger.DEBUG else TwineLogger.INFO
    }

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

            val v = p.getEyePosition(1.0f)
            return LuaVec3Instance(v.x, v.y, v.z)
        }

    @TwineNativeProperty
    val headRot: LuaVec3Instance
        get() {
            val p = instance.player
                ?: return LuaVec3Instance(0.0, 0.0, 0.0)

            return LuaVec3Instance(p.xRot.toDouble(), p.yRot.toDouble(), 0.0)
        }

    @TwineNativeFunction
    fun sendActionbar(message: String) {
        instance.player?.displayClientMessage(parseMini(message), true)
    }

    @TwineNativeFunction
    fun send(message: String) {
        instance.player?.displayClientMessage(parseMini(message), false)
    }

    @TwineNativeFunction
    fun setPerspective(perspective: String) {
        val enum = CameraType.valueOf(perspective)

        instance.options.cameraType = enum
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

        if (slot !in 0 until inv.containerSize) return null

        val stack = inv.getItem(slot)
        if (stack.isEmpty) return null

        return LuaItem().setStack(stack)
    }

    @TwineNativeFunction
    fun clearSlot(slot: Int) {
        val player = instance.player ?: return
        val inv = player.inventory

        if (slot !in 0 until inv.containerSize) return

        inv.setItem(slot, ItemStack.EMPTY)
    }

    @TwineNativeProperty
    val heldItem: LuaItem?
        get() {
            val player = instance.player ?: return null
            val stack = player.mainHandItem
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

    @TwineNativeFunction
    fun screenshotMessage(value: LuaTextInstance) {
        Client.customScreenshotMessage = value.toComponent()
        Client.customScreenshotInstance = value
    }

    /*
    * Camera Controls
    */

    @TwineNativeProperty
    var cameraLocked: Boolean
        get() = Client.cameraLocked
        set(value) { Client.cameraLocked = value }

    @TwineNativeFunction
    @TwineOverload
    fun lockCamera(value: Boolean) {
        Client.cameraLocked = value
    }

    @TwineNativeFunction
    @TwineOverload
    fun lockCamera() {
        Client.cameraLocked = true
    }

    /*
    * Mouse Controls
    */

    @TwineNativeProperty
    var mouseButtonsLocked: Boolean
        get() = Client.mouseButtonsLocked
        set(value) { Client.mouseButtonsLocked = value }

    @TwineNativeFunction
    @TwineOverload
    fun lockMouseButtons(value: Boolean) {
        Client.mouseButtonsLocked = value
    }

    @TwineNativeFunction
    @TwineOverload
    fun lockMouseButtons() {
        Client.mouseButtonsLocked = true
    }
}
