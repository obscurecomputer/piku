package computer.obscure.piku.mod.fabric.scripting.platform

import computer.obscure.piku.core.scripting.platform.CoreEntity
import computer.obscure.piku.core.scripting.platform.CoreEntityDto
import computer.obscure.piku.core.scripting.platform.CorePlayer
import computer.obscure.piku.core.scripting.platform.CorePlayerDto
import computer.obscure.piku.core.scripting.platform.Platform
import net.minecraft.client.Minecraft

class FabricPlatform : Platform() {
    init { current = this }

    override fun entityFromDto(dto: CoreEntityDto): CoreEntity? {
        val level = Minecraft.getInstance().level ?: return null
        val entity = level.entitiesForRendering().firstOrNull {
            it.uuid == dto.uuid
        }
        return entity?.let(::FabricEntity)
    }

    override fun playerFromDto(dto: CorePlayerDto): CorePlayer? {
        val level = Minecraft.getInstance().level ?: return null
        return level.getPlayerByUUID(dto.uuid)?.let(::FabricPlayer)
    }
}