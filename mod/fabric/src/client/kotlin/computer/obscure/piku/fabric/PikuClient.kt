package computer.obscure.piku.fabric

import computer.obscure.piku.mod.common.Piku
import computer.obscure.piku.mod.common.packets.clientbound.handlers.ReceiveDataHandler
import computer.obscure.piku.mod.common.packets.clientbound.handlers.ReceiveScriptHandler
import computer.obscure.piku.mod.common.packets.clientbound.handlers.ReceiveStateHandler
import computer.obscure.piku.mod.common.packets.clientbound.payloads.ReceiveDataPayload
import computer.obscure.piku.mod.common.packets.clientbound.payloads.ReceiveScriptPayload
import computer.obscure.piku.mod.common.packets.clientbound.payloads.ReceiveStatePayload
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

class PikuClient : ClientModInitializer {
    override fun onInitializeClient() {
        Piku.init()

        PayloadTypeRegistry.playS2C().register(ReceiveScriptPayload.TYPE, ReceiveScriptPayload.STREAM_CODEC)
        PayloadTypeRegistry.playS2C().register(ReceiveDataPayload.TYPE, ReceiveDataPayload.STREAM_CODEC)
        PayloadTypeRegistry.playS2C().register(ReceiveStatePayload.TYPE, ReceiveStatePayload.STREAM_CODEC)

        ClientPlayNetworking.registerGlobalReceiver(ReceiveScriptPayload.TYPE) { payload, _ ->
            ReceiveScriptHandler.handle(payload)
        }

        ClientPlayNetworking.registerGlobalReceiver(ReceiveDataPayload.TYPE) { payload, _ ->
            ReceiveDataHandler.handle(payload)
        }

        ClientPlayNetworking.registerGlobalReceiver(ReceiveStatePayload.TYPE) { payload, _ ->
            ReceiveStateHandler.handle(payload)
        }
    }
}
