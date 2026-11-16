package computer.obscure.piku.minestom.scripting.platform

import computer.obscure.piku.core.scripting.platform.CoreBlock
import computer.obscure.piku.core.scripting.platform.CoreBlockDto
import computer.obscure.piku.core.scripting.platform.CoreEntity
import computer.obscure.piku.core.scripting.platform.CoreEntityDto
import computer.obscure.piku.core.scripting.platform.CorePlayer
import computer.obscure.piku.core.scripting.platform.CorePlayerDto
import computer.obscure.piku.core.scripting.platform.Platform
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.block.Block

class MinestomPlatform : Platform() {
    init { current = this }

    override fun entity(dto: CoreEntityDto): CoreEntity? =
        MinecraftServer.getInstanceManager().instances
            .firstNotNullOfOrNull { it.getEntityByUuid(dto.uuid) }
            ?.let(::MinestomEntity)

    override fun player(dto: CorePlayerDto): CorePlayer? =
        MinecraftServer.getConnectionManager()
            .getOnlinePlayerByUuid(dto.uuid)
            ?.let(::MinestomPlayer)

    override fun block(dto: CoreBlockDto): CoreBlock? {
        val block = Block.fromKey(dto.key) ?: return null
        return MinestomBlock(block)
    }
}