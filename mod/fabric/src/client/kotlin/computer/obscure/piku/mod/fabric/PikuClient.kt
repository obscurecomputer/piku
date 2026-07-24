package computer.obscure.piku.mod.fabric

import computer.obscure.piku.mod.fabric.events.ClientHudRender
import computer.obscure.piku.mod.fabric.events.ClientPlayConnection
import computer.obscure.piku.mod.fabric.events.ClientTick
import computer.obscure.piku.mod.fabric.packets.CustomPacket
import computer.obscure.piku.mod.fabric.packets.clientbound.ReceiveDataPacket
import computer.obscure.piku.mod.fabric.packets.clientbound.ReceiveScriptPacket
import computer.obscure.piku.mod.fabric.packets.clientbound.ReceiveStatePacket
import computer.obscure.piku.mod.fabric.packets.clientbound.UnloadScriptsPacket
import computer.obscure.piku.mod.fabric.packets.serverbound.SendDataPacket
import computer.obscure.piku.mod.fabric.packets.serverbound.SendStatePacket
import computer.obscure.piku.mod.fabric.packets.serverbound.SendUnloadedPacket
import computer.obscure.piku.mod.fabric.scripting.engine.ClientLuaEngine
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.system.exitProcess

class PikuClient : ClientModInitializer {
    companion object {
        val LOGGER: Logger = LogManager.getLogger()

        val miniMessage = MiniMessage.miniMessage()

        var engine: ClientLuaEngine? = null
            private set

        fun warn(message: Any) {
            LOGGER.warn(formatLog(message))
        }

        fun error(message: Any) {
            LOGGER.error(formatLog(message))
        }

        fun info(message: Any) {
            LOGGER.info(formatLog(message))
        }

        fun debug(message: Any) {
            LOGGER.debug(formatLog(message))
        }

        fun formatLog(message: Any) = "[Piku] $message"
    }

    override fun onInitializeClient() {
        try {
            info("Instantiating Lua Engine...")
            engine = ClientLuaEngine()
            engine?.init()
            info("Engine started successfully.")
        } catch (e: Throwable) {
            error("Engine failed to start!")
            e.printStackTrace()
            exitProcess(1)
        }

        LOGGER.info("Client Initialized successfully!")
        InputHandler.init()
        ClientLifecycleEvents.CLIENT_STARTED.register { client ->
            InputHandler.registerCallbacks(client.window.handle())
        }

        s2c(ReceiveDataPacket.TYPE, ReceiveDataPacket.STREAM_CODEC)
        s2c(ReceiveScriptPacket.TYPE, ReceiveScriptPacket.STREAM_CODEC)
        s2c(ReceiveStatePacket.TYPE, ReceiveStatePacket.STREAM_CODEC)
        s2c(UnloadScriptsPacket.TYPE, UnloadScriptsPacket.STREAM_CODEC)

        c2s(SendDataPacket.TYPE, SendDataPacket.STREAM_CODEC)
        c2s(SendStatePacket.TYPE, SendStatePacket.STREAM_CODEC)
        c2s(SendUnloadedPacket.TYPE, SendUnloadedPacket.STREAM_CODEC)

        ClientPlayConnection.register()
        ClientTick.register()
        ClientHudRender.register()
    }

    fun <T : CustomPacket> s2c(type: CustomPacketPayload.Type<T>, codec: StreamCodec<FriendlyByteBuf, T>) {
        PayloadTypeRegistry.clientboundPlay().register(type, codec)
        ClientPlayNetworking.registerGlobalReceiver(type) { payload, _ -> payload.handle() }
    }

    fun <T : CustomPacketPayload> c2s(type: CustomPacketPayload.Type<T>, codec: StreamCodec<FriendlyByteBuf, T>) {
        PayloadTypeRegistry.serverboundPlay().register(type, codec)
    }
}