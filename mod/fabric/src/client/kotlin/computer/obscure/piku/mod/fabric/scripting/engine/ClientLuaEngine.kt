package computer.obscure.piku.mod.fabric.scripting.engine

import computer.obscure.piku.core.scripting.api.LocalScriptEvents
import computer.obscure.piku.core.scripting.api.NetEvents
import computer.obscure.piku.core.scripting.engine.LuaEngine
import computer.obscure.piku.core.scripting.events.EventBus
import computer.obscure.piku.core.scripting.events.NetBus
import computer.obscure.piku.core.utils.toJson
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.packets.serverbound.SendDataPacket
import computer.obscure.piku.mod.fabric.scripting.LuaStateManager
import computer.obscure.piku.mod.fabric.scripting.api.client.LuaClient
import computer.obscure.piku.mod.fabric.scripting.api.client.LuaGame
import computer.obscure.piku.mod.fabric.scripting.api.client.LuaLevel
import computer.obscure.piku.mod.fabric.scripting.api.controlify.LuaControlify
import computer.obscure.piku.mod.fabric.scripting.api.raycast.LuaRaycast
import computer.obscure.piku.mod.fabric.scripting.api.screen.LuaScreens
import computer.obscure.piku.mod.fabric.scripting.api.screen.LuaWidgets
import computer.obscure.piku.mod.fabric.scripting.api.sound.LuaSound
import computer.obscure.piku.mod.fabric.scripting.api.storage.LuaSessionStorage
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaEasing
import me.znotchill.kiwi.twine.TwineCompat
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class ClientLuaEngine : LuaEngine() {
    lateinit var clientEvents: EventBus
        private set

    lateinit var serverEvents: NetBus<Unit>
        private set

    private lateinit var clientScriptEvents: LocalScriptEvents
    private lateinit var serverScriptEvents: NetEvents<Unit>

    override fun shutdown() {
        PikuClient.info("Engine shut down")

        clientScriptEvents.disconnectAll()
        serverScriptEvents.disconnectAll()

        super.shutdown()
    }

    override fun init() {
        clientEvents = EventBus()

        serverEvents = NetBus { channel, data, _ ->
            val json = data.toJson()
            ClientPlayNetworking.send(SendDataPacket(id = channel, json = json))
        }

        clientScriptEvents = LocalScriptEvents(clientEvents)

        serverScriptEvents = NetEvents(
            bus = serverEvents,
            wrap = { null },
            unwrap = { }
        )

        super.init()

        register(clientScriptEvents)
        register(serverScriptEvents)

        register(LuaGame())
        register(LuaClient())
        register(LuaEasing())
        register(LuaStateManager())
        register(LuaScreens())
        register(LuaWidgets())
        register(LuaRaycast())
        register(LuaLevel())
        register(LuaSessionStorage())
        register(LuaControlify())
        register(LuaSound())
        TwineCompat.bind(engine)
        super.registerCommons()
    }
}