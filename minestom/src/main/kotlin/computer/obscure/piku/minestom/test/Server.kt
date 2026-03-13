package computer.obscure.piku.minestom.test

import computer.obscure.piku.core.classes.ScriptSource
import computer.obscure.piku.minestom.scripting.MinestomAPI
import computer.obscure.piku.minestom.scripting.states.getState
import computer.obscure.piku.minestom.scripting.states.sharedState
import me.znotchill.blossom.command.command
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
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
                source = ScriptSource.Resource(path = "scripts/client"),
                recurse = true
            )
        }

        registerCommand(
            command("share") {
                syntax {
                    val player = this
                    player.getState("hi")?.destroy()

                    val state = player.sharedState("hi") {
                        value = uuid.toString()
                        clientModifiable = true
                    }
                }
            }
        )

        registerCommand(
            command("update") {
                syntax {
                    val player = this
                    val state = player.getState("hi") ?: return@syntax
                    state.set(state.get().toString() + "_hi")
                }
            }
        )
    }
}

fun main() {
    Server().start()
}