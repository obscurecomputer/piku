package computer.obscure.piku.minestom.test

import computer.obscure.piku.core.classes.ScriptSource
import computer.obscure.piku.core.scripting.api.LuaEventData
import computer.obscure.piku.core.states.SharedState
import computer.obscure.piku.core.states.sharedState
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import computer.obscure.piku.minestom.scripting.MinestomAPI
import me.znotchill.blossom.command.command
import me.znotchill.blossom.extensions.ticks
import me.znotchill.blossom.scheduler.task
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerChunkLoadEvent
import net.minestom.server.event.player.PlayerChunkUnloadEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.instance.InstanceContainer
import java.util.UUID

class Server : BlossomServer(
    auth = false
) {
    lateinit var instanceContainer: InstanceContainer

    val piku = MinestomAPI(this)

    val states = mutableMapOf<Player, SharedState>()

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
                    if (states[this] != null)
                        states[this]!!.destroy()

                    val state = sharedState("hi") {
                        value = uuid.toString()
                        clientModifiable = true

                        owners = listOf(
                            this@syntax
                        )

                        onSet = { oldValue, newValue ->
                            sendMessage("$oldValue changed to $newValue!")
                        }
                    }

                    states[this] = state
                }
            }
        )

        registerCommand(
            command("update") {
                syntax {
                    if (states[this] == null) return@syntax

                    val state = states[this]!!
                    state.set(state.get().toString() + "_hi")
                }
            }
        )
    }
}

fun main() {
    Server().start()
}