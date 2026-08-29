package computer.obscure.piku.mod.fabric.events

import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.animation.AnimationManager
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.minecraft.resources.Identifier

object ClientHudRender {
    var lastTimeNano = System.nanoTime()

    fun register() {
        HudElementRegistry.addLast(
            Identifier.parse("piku:ui")
        ) { context, _ ->
            val currentTime = System.nanoTime()
            val deltaSeconds = minOf(
                (currentTime - lastTimeNano) / 1_000_000_000.0,
                0.1
            ) // capped
            lastTimeNano = currentTime

            PikuClient.engine!!.events.fire("client.ui_render", mapOf())

            AnimationManager.tick(deltaSeconds)
            UIRenderer.render(context)
        }
    }
}