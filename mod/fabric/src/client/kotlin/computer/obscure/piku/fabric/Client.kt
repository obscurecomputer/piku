package computer.obscure.piku.fabric

import computer.obscure.piku
import net.kyori.adventure.text.Component
import net.minecraft.client.CameraType
import net.minecraft.client.Minecraft

object Client {
    @JvmField
    var customPitch = 0f
    @JvmField
    var customYaw = 0f
    @JvmField
    var customRoll = 0f

    @JvmField
    var targetFov: Float = -1f
    @JvmField
    var currentFov: Int = Minecraft.getInstance().options.fov().get()
    @JvmField
    var lockFov: Boolean = false
    @JvmField
    var isInterpolatingFov: Boolean = false
    @JvmField
    var fovAnimTicks: Int = 5
    @JvmField
    var animateFov: Boolean = false
    @JvmField
    var previousFov: Float = -1f
    @JvmField
    var fovTicksRemaining: Int = 0

    @JvmField
    var cameraOffsetX = 0f
    @JvmField
    var cameraOffsetY = 0f
    @JvmField
    var cameraOffsetZ = 0f

    @JvmField
    var cameraLocked: Boolean = false
    @JvmField
    var mouseButtonsLocked: Boolean = false
    @JvmField
    var emitMouseEvents: Boolean = false

    @JvmField
    var isLeftClicking: Boolean = false
    @JvmField
    var isRightClicking: Boolean = false

    @JvmField
    var perspectiveLocked: Boolean = false
    var currentPerspective: CameraType
        get() = Minecraft.getInstance().options.cameraType
        set(value) {
            Minecraft.getInstance().options.cameraType = value
        }

    fun updateFovAnimation() {
        if (fovTicksRemaining > 0) {
            println(fovTicksRemaining)
            fovTicksRemaining--
        }
    }

    @JvmField
    var connectedToServer: Boolean = false
    @JvmField
    var serverRunsPiku: Boolean = false

    @JvmField
    var hideHUD = false
    @JvmField
    var hideHotbar = false
    @JvmField
    var hideArm = false

    @JvmField
    var customScreenshotMessage: Component? = null
    @JvmField
    var customScreenshotInstance: LuaTextInstance? = null
}