package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.piku.mod.fabric.scripting.api.util.minecraft.toLua
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineProperty
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.Entity

class LuaEntity(val entity: Entity) : TwineNative() {
    @TwineProperty
    val name: String = entity.displayName.string

    @TwineProperty
    val id: Int = entity.id

    @TwineProperty
    val uuid: String = entity.stringUUID

    @TwineProperty
    val type: String = entity.type.description.string

    @TwineProperty
    val x: Double = entity.x

    @TwineProperty
    val y: Double = entity.y

    @TwineProperty
    val z: Double = entity.z

    @TwineProperty
    val yaw: Float = entity.yRot

    @TwineProperty
    val pitch: Float = entity.xRot

    @TwineProperty
    val motionX: Double = entity.deltaMovement.x

    @TwineProperty
    val motionY: Double = entity.deltaMovement.y

    @TwineProperty
    val motionZ: Double = entity.deltaMovement.z

    @TwineProperty
    val motion: LuaVec3Instance = entity.deltaMovement.toLua()

    @TwineProperty
    val isOnGround: Boolean = entity.onGround()

    @TwineProperty
    val distanceToPlayer: Double = entity.distanceTo(
        Minecraft.getInstance().player!!
    ).toDouble()

    @TwineProperty
    val isSprinting: Boolean = entity.isSprinting

    @TwineProperty
    val isSneaking: Boolean = entity.isCrouching

    @TwineProperty
    val isSwimming: Boolean = entity.isSwimming

    @TwineProperty
    val isGlowing: Boolean = entity.isCurrentlyGlowing

    @TwineProperty
    val isOnFire: Boolean = entity.isOnFire

    @TwineProperty
    val isInWater: Boolean = entity.isInWater

    @TwineProperty
    val isInvisible: Boolean = entity.isInvisible

    @TwineProperty
    val data: LuaEntityComponents = LuaEntityComponents(entity)
}