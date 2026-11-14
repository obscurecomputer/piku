package computer.obscure.piku.minestom.test

import computer.obscure.piku.core.classes.ScriptSource
import computer.obscure.piku.minestom.scripting.MinestomAPI
import computer.obscure.piku.minestom.scripting.platform.MinestomPlayer
import computer.obscure.piku.minestom.scripting.states.sharedState
import computer.obscure.piku.minestom.scripting.utils.piku
import me.znotchill.kiwi.blossom.command.command
import me.znotchill.kiwi.blossom.extensions.addListener
import me.znotchill.kiwi.blossom.server.BlossomServer
import net.minestom.server.Auth
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.instance.InstanceContainer
import java.io.File
import kotlin.system.exitProcess

internal class Server : BlossomServer(
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

        val scripts = mutableMapOf<String, String>()
        piku.forEachFileScript(
            File("server/minestom/test/scripts/server"),
            true,
            consumer = { name, content ->
                scripts[name] = content
            }
        )
        scripts.forEach { (name, content) ->
            println(content)
            try {
                piku.engine.runScript(name, content)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        piku.hotReload(
            players = {
                this.players.toList()
            },
            source = ScriptSource.Directory(dir = File("server/minestom/test/scripts/client")),
            onSuccessfulReload = { players, files ->
                files.forEach {
                    players.forEach { p ->
                        p.sendMessage(it.file.name)
                    }
                }
                players.forEach { it.sendMessage("Hot reloaded ${files.size}!") }
            }
        )

        MinecraftServer.setBrandName("<gradient:#54daf4:#545eb6>hello!</gradient>")

        eventHandler.addListener<PlayerLoadedEvent> { event ->
            event.player.sharedState("test") {
                value = false
                clientModifiable = true
                onSet = { oldValue, newValue ->
                    event.player.sendMessage("$oldValue -> $newValue")
                }
            }
            piku.sendAllScripts(
                player = event.player,
                source = ScriptSource.Directory(dir = File("server/minestom/test/scripts/client")),
                recurse = true
            )

            piku.serverEvents.emit("server.player_join", mapOf(
                "player" to MinestomPlayer(event.player)
            ))
        }

        registerCommand(
            command("piku") {
                syntax {
                    sendMessage("using piku?: ${this.piku.usingPiku}")
                }
            }
        )

        registerCommand(command("stop") {
            syntax {
                Thread {
                    MinecraftServer.stopCleanly()
                }.start()

                MinecraftServer.getSchedulerManager().buildShutdownTask {
                    println("Process exiting")
                    exitProcess(0)
                }
            }
        })
    }
}

fun main() {
    Server().start()
}