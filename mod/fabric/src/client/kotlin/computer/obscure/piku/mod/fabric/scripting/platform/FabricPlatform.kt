package computer.obscure.piku.mod.fabric.scripting.platform

import computer.obscure.piku.core.scripting.platform.CoreBlock
import computer.obscure.piku.core.scripting.platform.CoreBlockDto
import computer.obscure.piku.core.scripting.platform.CoreEntity
import computer.obscure.piku.core.scripting.platform.CoreEntityDto
import computer.obscure.piku.core.scripting.platform.CorePlayer
import computer.obscure.piku.core.scripting.platform.CorePlayerDto
import computer.obscure.piku.core.scripting.platform.Platform
import net.minecraft.client.Minecraft

class FabricPlatform : Platform() {
    init { current = this }

    override fun entity(dto: CoreEntityDto): CoreEntity? {
        val level = Minecraft.getInstance().level ?: return null
        val entity = level.entitiesForRendering().firstOrNull {
            it.uuid == dto.uuid
        }
        return entity?.let(::FabricEntity)
    }

    override fun player(dto: CorePlayerDto): CorePlayer? {
        val level = Minecraft.getInstance().level ?: return null
        return level.getPlayerByUUID(dto.uuid)?.let(::FabricPlayer)
    }

    override fun block(dto: CoreBlockDto): CoreBlock? {
        TODO("Not yet implemented")
    }
}