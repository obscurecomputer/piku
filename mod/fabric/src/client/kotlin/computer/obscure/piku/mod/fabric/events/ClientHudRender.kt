package computer.obscure.piku.mod.fabric.events

import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.animation.AnimationManager
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback

object ClientHudRender {
    var lastTimeNano = System.nanoTime()

    fun register() {
        HudRenderCallback.EVENT.register { context, tickDelta ->
            AnimationManager.tick(tickDelta.gameTimeDeltaTicks / 20.0)
            val currentTime = System.nanoTime()
            val deltaSeconds = (currentTime - lastTimeNano) / 1_000_000_000.0
            lastTimeNano = currentTime

            UIRenderer.tickAnimations(deltaSeconds)

            UIRenderer.currentWindow.let { window ->
                UIRenderer.layoutIfNeeded(window)

                val sorted = window.components.values.toList().sortedBy {
                    it.props.zIndex
                }
                sorted.forEach {
                    UIRenderer.drawComponent(context, it)
                }

                val event = mapOf(
                    "rendered_components" to sorted.size,
                    "last_frame_delta" to deltaSeconds,
                    "current_time" to currentTime,
                    "active_animations" to UIRenderer.activeAnimations.size
                )
                PikuClient.engine!!.events.fire("client.ui_render", event)
            }
        }
    }
}