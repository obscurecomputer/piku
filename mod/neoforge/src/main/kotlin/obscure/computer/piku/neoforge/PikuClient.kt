package obscure.computer.piku.neoforge

import computer.obscure.piku.mod.common.Piku
import computer.obscure.piku.mod.common.packets.clientbound.handlers.ReceiveDataHandler
import computer.obscure.piku.mod.common.packets.clientbound.handlers.ReceiveScriptHandler
import computer.obscure.piku.mod.common.packets.clientbound.payloads.ReceiveDataPayload
import computer.obscure.piku.mod.common.packets.clientbound.payloads.ReceiveScriptPayload
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar

@Mod(Piku.MOD_ID)
class PikuClient(modBus: IEventBus) {
    init {
        modBus.addListener(this::onClientSetup)

        modBus.addListener(::registerNetworking)
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {
        Piku.init()
    }

    private fun registerNetworking(event: RegisterPayloadHandlersEvent) {
        val registrar: PayloadRegistrar = event.registrar("1.0").optional()

        registrar.playToClient(
            ReceiveScriptPayload.TYPE,
            ReceiveScriptPayload.STREAM_CODEC
        ) { payload, context ->
            context.enqueueWork {
                ReceiveScriptHandler.handle(payload)
            }
        }

        registrar.playToClient(
            ReceiveDataPayload.TYPE,
            ReceiveDataPayload.STREAM_CODEC
        ) { payload, context ->
            context.enqueueWork {
                ReceiveDataHandler.handle(payload)
            }
        }
    }
}