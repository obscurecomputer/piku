package computer.obscure.piku.mod.fabric.scripting.api

import com.mojang.blaze3d.platform.InputConstants
import computer.obscure.piku.core.scripting.api.LuaTextInstance
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.piku.mod.fabric.Client
import computer.obscure.piku.mod.fabric.InputHandler
import computer.obscure.piku.mod.fabric.utils.parseMini
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import net.minecraft.client.CameraType
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack

class LuaClient : TwineNative("client") {
    val instance: Minecraft = Minecraft.getInstance()

//    @TwineFunction
//    fun debug(value: Boolean) {
//        TwineLogger.level = if (value) TwineLogger.DEBUG else TwineLogger.INFO
//    }

    @TwineProperty
    val pos: LuaVec3Instance
        get() {
            val p = instance.player
                ?: return LuaVec3Instance(0.0, 0.0, 0.0)

            return LuaVec3Instance(p.x, p.y, p.z)
        }

    @TwineProperty
    val headPos: LuaVec3Instance
        get() {
            val p = instance.player
                ?: return LuaVec3Instance(0.0, 0.0, 0.0)

            val v = p.getEyePosition(1.0f)
            return LuaVec3Instance(v.x, v.y, v.z)
        }

    @TwineProperty
    val headRot: LuaVec3Instance
        get() {
            val p = instance.player
                ?: return LuaVec3Instance(0.0, 0.0, 0.0)

            return LuaVec3Instance(p.xRot.toDouble(), p.yRot.toDouble(), 0.0)
        }

    @TwineFunction
    fun sendActionbar(message: Any) {
        instance.player?.displayClientMessage(parseMini(message.toString()), true)
    }

    @TwineFunction
    fun send(message: Any) {
        instance.player?.displayClientMessage(parseMini(message.toString()), false)
    }

    @TwineFunction
    fun setPerspective(perspective: String) {
        val enum = CameraType.valueOf(perspective)

        instance.options.cameraType = enum
    }

    @TwineProperty
    var hideHotbar: Boolean
        get() = Client.hideHotbar
        set(value) { Client.hideHotbar = value }

    @TwineProperty
    var hideArm: Boolean
        get() = Client.hideArm
        set(value) { Client.hideArm = value }

    @TwineProperty
    var hideHUD: Boolean
        get() = Client.hideHUD
        set(value) {
            instance.options.hideGui = value
            Client.hideHUD = value
        }

    @TwineProperty
    var selectedSlot: Int
        get() = instance.player?.inventory?.selectedSlot ?: 0
        set(value) {
            instance.player?.inventory?.selectedSlot = value.coerceIn(0, 8)
        }

    @TwineFunction
    fun getItem(slot: Int): LuaItem? {
        val player = instance.player ?: return null
        val inv = player.inventory

        if (slot !in 0 until inv.containerSize) return null

        val stack = inv.getItem(slot)
        if (stack.isEmpty) return null

        return LuaItem().setStack(stack)
    }

    @TwineFunction
    fun clearSlot(slot: Int) {
        val player = instance.player ?: return
        val inv = player.inventory

        if (slot !in 0 until inv.containerSize) return

        inv.setItem(slot, ItemStack.EMPTY)
    }

    @TwineProperty
    val heldItem: LuaItem?
        get() {
            val player = instance.player ?: return null
            val stack = player.mainHandItem
            return if (stack.isEmpty) null
            else LuaItem().setStack(stack)
        }

    @TwineProperty
    val windowSize: LuaVec2Instance
        get() {
            val x = instance.window.width.toDouble()
            val y = instance.window.height.toDouble()
            return LuaVec2Instance(x, y)
        }

    @TwineFunction
    fun screenshotMessage(value: LuaTextInstance) {
        Client.customScreenshotMessage = value.toComponent()
        Client.customScreenshotInstance = value
    }

    /*
    * Camera Controls
    */

    @TwineProperty
    var cameraLocked: Boolean
        get() = Client.cameraLocked
        set(value) { Client.cameraLocked = value }

    @TwineFunction
    fun lockCamera(value: Boolean) {
        Client.cameraLocked = value
    }

    @TwineFunction
    fun lockCamera() {
        Client.cameraLocked = true
    }

    /*
    * Mouse Controls
    */

    @TwineProperty
    var mouseButtonsLocked: Boolean
        get() = Client.mouseButtonsLocked
        set(value) { Client.mouseButtonsLocked = value }

    @TwineFunction
    fun lockMouseButtons(value: Boolean) {
        Client.mouseButtonsLocked = value
    }

    @TwineFunction
    fun lockMouseButtons() {
        Client.mouseButtonsLocked = true
    }

    @TwineFunction
    fun playSound(name: String, volume: Double, pitch: Double) {
        val player = instance.player ?: return
        val Identifier = Identifier.tryParse(name) ?: return

        val soundEvent = SoundEvent.createVariableRangeEvent(Identifier)
        val soundInstance = SimpleSoundInstance(
            soundEvent,
            SoundSource.PLAYERS,
            volume.toFloat(),
            pitch.toFloat(),
            player.random,
            player.x,
            player.y,
            player.z
        )

        instance.soundManager.play(soundInstance)
    }

    @TwineFunction
    fun playSound(name: String) {
        playSound(name, 1.0, 1.0)
    }

    @TwineFunction
    fun getKeybind(name: String): LuaKeyBind? {
        val bind = instance.options.keyMappings.find { it.name == name }
            ?: return null

        val internalName = bind.saveString()

        val boundName = when {
            internalName.startsWith("key.keyboard.") -> {
                val keyCode = InputConstants.getKey(internalName).value
                InputHandler.getKeyName(keyCode)
            }
            internalName.startsWith("key.mouse.") -> {
                val keyCode = InputConstants.getKey(internalName).value
                InputHandler.getMouseButtonName(keyCode)
            }
            else -> "unknown"
        }

        return LuaKeyBind(
            name = bind.name,
            isDown = bind.isDown,
            isUnbound = bind.isUnbound,
            isDefault = bind.isDefault,
            category = bind.category.id.toString(),
            boundKey = boundName
        )
    }

    @TwineProperty
    var bobbing: Boolean
        get() {
            println("CALLED")
            return instance.options.bobView().get()
        }
        set(value) {
            println("setting to $value")
            instance.options.bobView().set(value)
        }

    @TwineProperty
    var bobbingStrength: Float
        get() = Client.bobbingStrength
        set(value) {
            Client.bobbingStrength = value
        }
}
