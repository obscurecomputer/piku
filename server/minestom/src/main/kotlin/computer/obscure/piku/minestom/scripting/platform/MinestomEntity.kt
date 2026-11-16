package computer.obscure.piku.minestom.scripting.platform

import computer.obscure.piku.core.scripting.platform.CoreEntity
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineProperty
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import java.util.*
import computer.obscure.piku.core.classes.Pos as CorePos
import me.znotchill.kiwi.generated.Vec3 as CoreVec3

open class MinestomEntity(
    val entity: Entity
) : CoreEntity, TwineNative() {
    override val uuid: UUID = entity.uuid

    @TwineProperty
    override var position: CorePos
        get() = CorePos(
            CoreVec3(
                entity.position.x,
                entity.position.y,
                entity.position.z
            ),
            pitch = entity.position.pitch,
            yaw = entity.position.yaw,
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