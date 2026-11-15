package computer.obscure.piku.mod.fabric.scripting.platform

import computer.obscure.piku.core.scripting.platform.CorePlayer
import computer.obscure.piku.core.scripting.platform.RefFactory
import computer.obscure.piku.core.scripting.platform.RefKinds
import computer.obscure.piku.mod.fabric.utils.toNativeComponent
import net.kyori.adventure.text.Component
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import java.util.UUID

class FabricPlayer(
    val player: Player
) : FabricEntity(player), CorePlayer<EntityType<*>> {
    override fun send(component: Component) {
        player.sendSystemMessage(component.toNativeComponent())
    }

    companion object : RefFactory {
        override val kind = RefKinds.PLAYER

        override fun resolve(uuid: UUID): FabricPlayer? {
            return Minecraft.getInstance().level
                ?.getPlayerByUUID(uuid)
                ?.let { FabricPlayer(it) }
        }
    }
}