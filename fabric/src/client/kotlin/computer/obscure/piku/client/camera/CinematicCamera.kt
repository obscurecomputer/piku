package computer.obscure.piku.client.camera

import net.minecraft.util.math.Vec3d

object CinematicCamera {
    var active = false

    var pos = Vec3d.ZERO
    var yaw = 0f
    var pitch = 0f
    var fov = 70f

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