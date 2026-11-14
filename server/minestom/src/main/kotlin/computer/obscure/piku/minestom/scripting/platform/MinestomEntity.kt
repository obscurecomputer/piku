package computer.obscure.piku.minestom.scripting.platform

import computer.obscure.piku.core.classes.Pos as CorePos
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineProperty
import net.minestom.server.coordinate.Pos
import me.znotchill.kiwi.generated.Vec3 as CoreVec3
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import computer.obscure.piku.core.scripting.platform.Entity as CoreEntity

class MinestomEntity(
    val entity: Entity
) : CoreEntity<EntityType>, TwineNative() {

    override val type: EntityType
        get() = entity.entityType

    @TwineProperty
    override var position: CorePos
        get() = CorePos(
            CoreVec3(
                entity.position.x,
                entity.position.y,
                entity.position.z
            ),
            pitch = entity.position.pitch,
            yaw = entity.position.pitch,
        )
        set(value) {
            val pos = Pos(
                value.vec.x,
                value.vec.y,
                value.vec.z,
                value.pitch,
                value.yaw
            )
            entity.teleport(pos)
        }
}