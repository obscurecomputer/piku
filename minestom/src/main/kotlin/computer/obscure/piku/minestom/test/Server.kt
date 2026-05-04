package computer.obscure.piku.minestom.test

import computer.obscure.piku.core.classes.ScriptSource
import computer.obscure.piku.minestom.scripting.MinestomAPI
import computer.obscure.piku.minestom.scripting.states.getState
import computer.obscure.piku.minestom.scripting.states.sharedState
import me.znotchill.blossom.command.command
import me.znotchill.blossom.component.component
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.Audiences
import net.minestom.server.Auth
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.instance.InstanceContainer
import java.io.File

class Server : BlossomServer(
    auth = Auth.Offline()
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

        piku.hotReload(
            players = {
                this.players.toList()
            },
            source = ScriptSource.Directory(dir = File("minestom/test/scripts/client")),
            onSuccessfulReload = { players ->
                val audience = Audience.audience(players)
                audience.sendMessage(
                    component {
                        text("Hot reloaded!")
                    }
                )
            }
        )


        eventHandler.addListener<PlayerLoadedEvent> { event ->
            piku.sendAllScripts(
                player = event.player,
                source = ScriptSource.Directory(dir = File("minestom/test/scripts/client")),
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