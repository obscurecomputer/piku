package computer.obscure.piku.minestom.test

import computer.obscure.piku.common.scripting.api.LuaEventData
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import computer.obscure.piku.minestom.scripting.MinestomAPI
import me.znotchill.blossom.extensions.ticks
import me.znotchill.blossom.scheduler.task
import net.minestom.server.entity.GameMode
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

    val loadedChunks = mutableMapOf<UUID, MutableSet<Long>>()

    fun pack(x: Int, z: Int): Long =
        (x.toLong() shl 32) or (z.toLong() and 0xffffffff)

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

            scheduler.task {
                delay = 1.ticks
                run = {
                    piku.sendData(
                        event.player,
                        "load_map",
                        LuaEventData(
                            emptyMap()
                        )
                    )
                }
            }
        }

        eventHandler.addListener<PlayerChunkUnloadEvent> { event ->
            val set = loadedChunks[event.player.uuid] ?: return@addListener
            set.remove(pack(event.chunkX, event.chunkZ))
        }

        eventHandler.addListener<PlayerChunkLoadEvent> { event ->
            val player = event.player
            val uuid = player.uuid

            val set = loadedChunks.getOrPut(uuid) { mutableSetOf() }
            set.add(pack(event.chunkX, event.chunkZ))

            val loaded = set.size

            val px = player.chunk?.chunkX ?: 0
            val pz = player.chunk?.chunkX ?: 0

            val radius = set.maxOf {
                val x = (it shr 32).toInt()
                val z = it.toInt()
                maxOf(kotlin.math.abs(x - px), kotlin.math.abs(z - pz))
            }

            val total = (radius * 2 + 1) * (radius * 2 + 1)

            piku.sendData(
                player,
                "load_map_progress",
                LuaEventData(
                    mapOf(
                        "loaded" to loaded,
                        "total" to total
                    )
                )
            )

            if (total == loaded) {
                piku.sendData(
                    player,
                    "load_map_done",
                    LuaEventData(
                        emptyMap()
                    )
                )
            }
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