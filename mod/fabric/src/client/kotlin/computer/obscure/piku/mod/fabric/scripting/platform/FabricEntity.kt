package computer.obscure.piku.mod.fabric.scripting.platform

import computer.obscure.piku.core.scripting.platform.CoreEntity
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineProperty
import net.minecraft.world.entity.Entity
import java.util.UUID
import computer.obscure.piku.core.classes.Pos as CorePos
import me.znotchill.kiwi.generated.Vec3 as CoreVec3

open class FabricEntity(
    val entity: Entity
) : CoreEntity, TwineNative() {

    override val uuid: UUID = entity.uuid

    @TwineProperty
    override var position: CorePos
        get() {
            val p = entity.position()

            return CorePos(
                CoreVec3(p.x, p.y, p.z),
                pitch = entity.xRot,
                yaw = entity.yRot
            )
        }
        set(value) {
            entity.setPos(value.vec.x, value.vec.y, value.vec.z)
            entity.xRot = value.pitch
            entity.yRot = value.yaw
        }
}