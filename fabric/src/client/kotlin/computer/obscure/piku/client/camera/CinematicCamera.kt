package computer.obscure.piku.client.camera

import computer.obscure.piku.common.classes.Vec3
import net.minecraft.util.math.Vec3d

object CinematicCamera {
    var active = false

    var pos = Vec3d.ZERO
    var prevPos = Vec3d.ZERO

    var pitch = 0f
    var prevPitch = 0f

    var yaw = 0f
    var prevYaw = 0f

    var roll = 0f

    var fov = 70f

    var rotation: Vec3
        get() = Vec3(pitch, yaw, roll)
        set(value) {
            pitch = value.x.toFloat()
            yaw = value.y.toFloat()
            roll = value.z.toFloat()
        }

    fun enableFromPlayer() {
        val client = net.minecraft.client.MinecraftClient.getInstance()
        val player = client.player ?: return

        pos = player.getCameraPosVec(1f)
        yaw = player.yaw
        pitch = player.pitch
        fov = client.options.fov.value.toFloat()

        active = true
    }

    fun enable() {
        active = true
    }

    fun disable() {
        active = false
    }
}