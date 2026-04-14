package computer.obscure.piku.mod.fabric.raycast

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.absoluteValue

object Raycast {
    fun shoot(
        level: Level,
        start: Vec3,
        direction: Vec3,
        maxDistance: Double = 200.0,
        travelStep: Double = 0.05,
        nearbyEntityRadius: Double = 3.0,
        entityFilter: (Entity) -> Boolean = { true }
    ): RaycastResult {
        val dir = direction.normalize()
        val maxSteps = (maxDistance / travelStep).toInt()

        for (step in 0..maxSteps) {
            val rayPos = start.add(dir.scale(step * travelStep))
            val blockPos = BlockPos.containing(rayPos)

            if (!level.hasChunkAt(blockPos)) {
                return createEmptyResult(rayPos, hitUnloadedChunk = true)
            }

            val searchBox = AABB.unitCubeFromLowerCorner(rayPos).inflate(nearbyEntityRadius)
            val entities = level.getEntities(null, searchBox) { entityFilter(it) }
            
            val hitEntity = entities.find { it.boundingBox.inflate(0.3).contains(rayPos) }
            if (hitEntity != null) {
                return createEntityResult(rayPos, hitEntity)
            }

            val state = level.getBlockState(blockPos)
            if (!state.isAir && state.isSolid) {
                val blockCenter = Vec3.atCenterOf(blockPos)
                val offset = rayPos.subtract(blockCenter)
                val epsilon = 0.005

                val hitFace = when {
                    offset.x.absoluteValue + epsilon > offset.y.absoluteValue &&
                    offset.x.absoluteValue + epsilon > offset.z.absoluteValue ->
                        if (offset.x > 0) Direction.EAST else Direction.WEST
                    offset.y.absoluteValue + epsilon > offset.x.absoluteValue &&
                    offset.y.absoluteValue + epsilon > offset.z.absoluteValue ->
                        if (offset.y > 0) Direction.UP else Direction.DOWN
                    else ->
                        if (offset.z > 0) Direction.SOUTH else Direction.NORTH
                }

                return RaycastResult(
                    hitBlock = true,
                    hitBlockPosition = blockPos,
                    hitBlockState = state,
                    hitEntity = false,
                    entityHit = null,
                    hitUnloadedChunk = false,
                    hitMaxDistance = false,
                    hitPosition = rayPos,
                    relativeHitPosition = offset,
                    hitFace = hitFace
                )
            }
        }

        return createEmptyResult(start.add(dir.scale(maxDistance)), hitMaxDistance = true)
    }

    fun createEmptyResult(
        pos: Vec3 = Vec3.ZERO,
        hitUnloadedChunk: Boolean = false,
        hitMaxDistance: Boolean = false
    ) = RaycastResult(
        false, BlockPos.ZERO, Minecraft.getInstance().level!!.getBlockState(BlockPos.ZERO),
        false, null, hitUnloadedChunk, hitMaxDistance, pos, Vec3.ZERO, Direction.UP
    )

    private fun createEntityResult(pos: Vec3, entity: Entity) = RaycastResult(
        false, BlockPos.ZERO, entity.level().getBlockState(BlockPos.ZERO),
        true, entity, false, hitMaxDistance = false, hitPosition = pos, relativeHitPosition = Vec3.ZERO, hitFace = Direction.UP
    )
}