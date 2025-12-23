package me.znotchill.piku.minestom.test

import io.netty.buffer.ByteBufAllocator
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import me.znotchill.piku.common.utils.writeString
import me.znotchill.piku.minestom.scripting.MinestomAPI
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.network.packet.server.common.PluginMessagePacket

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
            val name = "test.lua"
            val content = """
                print("hi")
                
                listen("client.key_update", function(event)
                    print(event.key)
                    print("hsdadsa")
                    
                    send("test.bro", { awesome = 5 })
                end)
            """.trimIndent()

            piku.sendScript(event.player, name, content)

            piku.runScript("test.lua", """
                print("HELLO FROM MINESTOM!!")
                
                listen("test.bro", function(event)
                    print("HI!!!! NO WAY")
                end)
            """.trimIndent())
        }
    }
}

fun main() {
    Server().start()
}