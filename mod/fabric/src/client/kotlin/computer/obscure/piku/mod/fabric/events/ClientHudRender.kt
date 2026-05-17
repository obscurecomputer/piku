package computer.obscure.piku.mod.fabric.events

import computer.obscure.piku.core.animation.AnimationManager
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback

object ClientHudRender {
    var lastTimeNano = System.nanoTime()

    fun register() {
        HudRenderCallback.EVENT.register { context, tickDelta ->
            val currentTime = System.nanoTime()
            val deltaSeconds = (currentTime - lastTimeNano) / 1_000_000_000.0
            lastTimeNano = currentTime

            AnimationManager.tick(deltaSeconds)
            UIRenderer.render(context)
        }
    }
}