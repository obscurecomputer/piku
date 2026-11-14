package computer.obscure.piku.mod.fabric.raycast

import me.znotchill.kiwi.generated.Vec3
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3 as MCVec3
import net.minecraft.world.phys.shapes.CollisionContext

private fun Vec3.toMc() = MCVec3(x, y, z)

object Raycast {

    fun shoot(
        level: Level,
        start: Vec3,
        direction: Vec3,
        maxDistance: Double = 200.0,
        nearbyEntityRadius: Double = 3.0,
        entityFilter: (Entity) -> Boolean = { true }
    ): RaycastResult {
        val origin = start.toMc()
        val end = (start + direction.normalize().scale(maxDistance)).toMc()
        val blockContext = ClipContext(
            origin,
            end,
            ClipContext.Block.COLLIDER,
            ClipContext.Fluid.NONE,
            CollisionContext.empty()
        )

        val blockHit: BlockHitResult = level.clip(blockContext)
        val hitDistSq = if (blockHit.type != HitResult.Type.MISS) {
            blockHit.location.distanceToSqr(origin)
        } else {
            Double.MAX_VALUE
        }

        val searchBox = AABB(origin, end).inflate(nearbyEntityRadius)
        val entities = level.getEntities(null, searchBox) {
            entityFilter(it)
        }

        var closestEntity: Entity? = null
        var entityHitVec: MCVec3? = null
        var closestEntityDistSq = hitDistSq

        for (entity in entities) {
            val aabb = entity.boundingBox.inflate(0.3)
            val clip = aabb.clip(origin, end)
            if (clip.isPresent) {
                val distSq = origin.distanceToSqr(clip.get())
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
                    relativeHitPosition = blockHit.location.subtract(MCVec3.atCenterOf(pos)),
                    hitFace = blockHit.direction
                )
            }
            else -> createEmptyResult(end, hitMaxDistance = true)
        }
    }

    fun createEmptyResult(
        pos: MCVec3 = MCVec3.ZERO,
        hitUnloadedChunk: Boolean = false,
        hitMaxDistance: Boolean = false
    ) = RaycastResult(
        false, BlockPos.ZERO, Minecraft.getInstance().level!!.getBlockState(BlockPos.ZERO),
        false, null, hitUnloadedChunk, hitMaxDistance, pos, MCVec3.ZERO, Direction.UP
    )

    private fun createEntityResult(pos: MCVec3, entity: Entity) = RaycastResult(
        false, BlockPos.ZERO, entity.level().getBlockState(BlockPos.ZERO),
        true, entity, false, hitMaxDistance = false, hitPosition = pos, relativeHitPosition = MCVec3.ZERO, hitFace = Direction.UP
    )
}