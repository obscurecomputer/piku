package computer.obscure.piku.minestom.test

import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import computer.obscure.piku.minestom.scripting.MinestomAPI
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.instance.InstanceContainer

class Server : BlossomServer(
    auth = false
) {
    lateinit var instanceContainer: InstanceContainer

    val piku = MinestomAPI(this)

    override fun preLoad() {
        piku.registerEvents()
        instanceContainer = BaseInstance().createInstance(instanceManager)

        eventHandler.addListener<AsyncPlayerConfigurationEvent> { event ->
            val player = event.player
            event.spawningInstance = instanceContainer
            player.gameMode = GameMode.ADVENTURE
            player.permissionLevel = 4
        }

        eventHandler.addListener<PlayerLoadedEvent> { event ->
            piku.sendAllScripts(
                player = event.player,
                resourceDirectory = "scripts/client",
                recurse = true
            )
        }

        piku.runAllScripts(
            resourceDirectory = "scripts/server",
            recurse = true
        )
    }
}

fun main() {
    Server().start()
}