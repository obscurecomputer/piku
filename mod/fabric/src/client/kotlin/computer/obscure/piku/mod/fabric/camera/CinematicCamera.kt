package computer.obscure.piku.mod.fabric.camera

import computer.obscure.piku.core.classes.Vec3
import computer.obscure.piku.mod.fabric.scripting.api.util.minecraft.toCoreVec3
import net.minecraft.client.Minecraft

object CinematicCamera {
    var active = false

    var pos = Vec3.ZERO
    var prevPos = Vec3.ZERO

    var pitch = 0.0
    var prevPitch = 0.0

    var yaw = 0.0
    var prevYaw = 0.0

    var roll = 0.0

    var fov = 70

    var rotation: Vec3
        get() = Vec3(pitch, yaw, roll)
        set(value) {
            pitch = value.x
            yaw = value.y
            roll = value.z
        }

    fun enableFromPlayer() {
        val client = Minecraft.getInstance()
        val player = client.player ?: return

        pos = player.getEyePosition(1f).toCoreVec3()
        yaw = player.yRot.toDouble()
        pitch = player.xRot.toDouble()
        fov = client.options.fov().get()

        active = true
    }

    fun enable() {
        active = true
    }

    fun disable() {
        active = false
    }
}