package computer.obscure.piku.mod.fabric

import computer.obscure.piku.core.classes.Vec3
import computer.obscure.piku.core.scripting.api.LuaTextInstance
import net.kyori.adventure.text.Component
import net.minecraft.client.CameraType
import net.minecraft.client.Minecraft

object Client {
    @JvmField
    var rotation: Vec3 = Vec3.ZERO

    @JvmField
    var targetFov: Float = -1f
    @JvmField
    var currentFov: Float = Minecraft.getInstance().options.fov().get().toFloat()
    @JvmField
    var animatedFov: Float = -1f
    @JvmField
    var vanillaFov: Float = -1f
    @JvmField
    var lockFov: Boolean = false
    @JvmField
    var isInterpolatingFov: Boolean = false
    @JvmField
    // true = animation system owns currentFov
    var fovControlled: Boolean = false
    @JvmField
    var fovAnimTicks: Int = 5
    @JvmField
    var animateFov: Boolean = false
    @JvmField
    var previousFov: Int = -1
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

    @JvmField
    var bobbingStrength: Float = 0f

}