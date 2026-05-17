package computer.obscure.piku.paper

import com.github.retrooper.packetevents.PacketEvents
import computer.obscure.piku.core.classes.ScriptSource
import computer.obscure.piku.paper.scripting.PaperAPI
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class PikuPaper : JavaPlugin() {
    val piku = PaperAPI(
        plugin = this,
        scriptDir = File(dataFolder, "scripts")
    )

    override fun onEnable() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this))
        PacketEvents.getAPI().load()
        PacketEvents.getAPI().init()
        dataFolder.mkdirs()
        piku.scriptDir.mkdirs()

        listOf("scripts/hello.luau").forEach { path ->
            if (!File(dataFolder, path).exists()) saveResource(path, false)
        }

        piku.registerEvents()

        piku.hotReload(
            players = { server.onlinePlayers.toList() },
            source = ScriptSource.Directory(dir = piku.scriptDir),
            onSuccessfulReload = {}
        )
    }

    override fun onDisable() {
        PacketEvents.getAPI().terminate()
    }
}