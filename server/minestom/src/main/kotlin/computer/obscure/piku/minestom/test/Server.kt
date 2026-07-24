package computer.obscure.piku.minestom.test

import computer.obscure.piku.core.classes.ScriptSource
import computer.obscure.piku.minestom.scripting.MinestomAPI
import computer.obscure.piku.minestom.scripting.utils.piku
import me.znotchill.blossom.command.command
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import me.znotchill.kiwi.generated.Color
import net.minestom.server.Auth
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerChatEvent
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

        eventHandler.addListener<PlayerLoadedEvent> { event ->
            piku.sendAllScripts(
                player = event.player,
                source = ScriptSource.Directory(dir = File("server/minestom/test/scripts/client")),
                recurse = true
            )
        }

        eventHandler.addListener<PlayerChatEvent> { event ->
            piku.sendData(
                players,
                "piku.demo.chat_message",
                mapOf(
                    "message" to event.rawMessage,
                    "author" to event.player.username,
                    "author_color" to Color.random(),
                )
            )
        }

        registerCommand(
            command("piku") {
                syntax {
                    sendMessage("using piku?: ${this.piku.usingPiku}")
                }
            }
        )
    }
}

fun main() {
    Server().start()
}