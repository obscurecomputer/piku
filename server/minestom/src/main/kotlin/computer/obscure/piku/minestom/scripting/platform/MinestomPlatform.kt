package computer.obscure.piku.minestom.scripting.platform

import computer.obscure.piku.core.scripting.platform.CoreEntity
import computer.obscure.piku.core.scripting.platform.CoreEntityDto
import computer.obscure.piku.core.scripting.platform.CorePlayer
import computer.obscure.piku.core.scripting.platform.CorePlayerDto
import computer.obscure.piku.core.scripting.platform.Platform
import net.minestom.server.MinecraftServer

class MinestomPlatform : Platform() {
    init { current = this }

    override fun entityFromDto(dto: CoreEntityDto): CoreEntity? =
        MinecraftServer.getInstanceManager().instances
            .firstNotNullOfOrNull { it.getEntityByUuid(dto.uuid) }
            ?.let(::MinestomEntity)

    override fun playerFromDto(dto: CorePlayerDto): CorePlayer? =
        MinecraftServer.getConnectionManager()
            .getOnlinePlayerByUuid(dto.uuid)
            ?.let(::MinestomPlayer)
}