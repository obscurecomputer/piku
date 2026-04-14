package computer.obscure.piku.mod.fabric.raycast

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext

object Raycast {

    fun shoot(
        level: Level,
        start: Vec3,
        direction: Vec3,
        maxDistance: Double = 200.0,
        nearbyEntityRadius: Double = 3.0,
        entityFilter: (Entity) -> Boolean = { true }
    ): RaycastResult {
        val end = start.add(direction.normalize().scale(maxDistance))

        val blockContext = ClipContext(
            start,
            end,
            ClipContext.Block.COLLIDER,
            ClipContext.Fluid.NONE,
            CollisionContext.empty()
        )

        val blockHit: BlockHitResult = level.clip(blockContext)
        val hitDistSq = if (blockHit.type != HitResult.Type.MISS) {
            blockHit.location.distanceToSqr(start)
        } else {
            Double.MAX_VALUE
        }

        val searchBox = AABB(start, end).inflate(nearbyEntityRadius)
        val entities = level.getEntities(null, searchBox) { entityFilter(it) }

        var closestEntity: Entity? = null
        var entityHitVec: Vec3? = null
        var closestEntityDistSq = hitDistSq

        for (entity in entities) {
            val aabb = entity.boundingBox.inflate(0.3)
            val clip = aabb.clip(start, end)
            if (clip.isPresent) {
                val distSq = start.distanceToSqr(clip.get())
                if (distSq < closestEntityDistSq) {
                    closestEntityDistSq = distSq
                    closestEntity = entity
                    entityHitVec = clip.get()
                }
            }
        }

        return when {
            closestEntity != null -> {
                createEntityResult(entityHitVec!!, closestEntity)
            }
            blockHit.type != HitResult.Type.MISS -> {
                val pos = blockHit.blockPos
                RaycastResult(
                    hitBlock = true,
                    hitBlockPosition = pos,
                    hitBlockState = level.getBlockState(pos),
                    hitEntity = false,
                    entityHit = null,
                    hitUnloadedChunk = false,
                    hitMaxDistance = false,
                    hitPosition = blockHit.location,
                    relativeHitPosition = blockHit.location.subtract(Vec3.atCenterOf(pos)),
                    hitFace = blockHit.direction
                )
            }
            else -> createEmptyResult(end, hitMaxDistance = true)
        }
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