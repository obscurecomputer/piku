package computer.obscure.piku.mod.fabric.raycast

import computer.obscure.piku.mod.fabric.scripting.api.raycast.LuaRaycastResult
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

data class RaycastResult(
    val hitBlock: Boolean,
    val hitBlockPosition: BlockPos,
    val hitBlockState: BlockState,
    val hitEntity: Boolean,
    val entityHit: Entity?,
    val hitUnloadedChunk: Boolean,
    val hitMaxDistance: Boolean,
    val hitPosition: Vec3,
    val relativeHitPosition: Vec3,
    val hitFace: Direction
) {
    fun toLua(): LuaRaycastResult = LuaRaycastResult(this)
}