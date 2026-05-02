package computer.obscure.piku.mod.fabric.scripting.api.raycast

import computer.obscure.piku.mod.fabric.raycast.RaycastResult
import computer.obscure.piku.mod.fabric.scripting.api.LuaBlockState
import computer.obscure.piku.mod.fabric.scripting.api.LuaEntity
import computer.obscure.piku.mod.fabric.scripting.api.util.minecraft.toCoreVec3
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty

class LuaRaycastResult(result: RaycastResult) : TwineNative() {
    // TODO: change block & entity to use their respective lua objects
    // when I add them
    @TwineProperty
    val hitBlock = result.hitBlock
    @TwineProperty
    val hitBlockPosition = result.hitBlockPosition.toCoreVec3()
    @TwineProperty
    val hitBlockState = LuaBlockState(result.hitBlockState)
    @TwineProperty
    val hitEntity = result.hitEntity
    @TwineProperty
    val entityHit: LuaEntity? =
        if (result.hitEntity)
            LuaEntity(result.entityHit!!)
        else null
    @TwineProperty
    val hitUnloadedChunk = result.hitUnloadedChunk
    @TwineProperty
    val hitMaxDistance = result.hitMaxDistance
    @TwineProperty
    val hitPosition = result.hitPosition.toCoreVec3()
    @TwineProperty
    val relativeHitPosition = result.relativeHitPosition.toCoreVec3()
    @TwineProperty
    val hitFace = result.hitFace.name

    @TwineFunction("tostring")
    override fun toString(): String {
        return "raycastResult[" +
                "hitBlock=$hitBlock, " +
                "hitBlockPosition=$hitBlockPosition, " +
                "hitBlockState=$hitBlockState, " +
                "hitEntity=$hitEntity, " +
                "entityHit=$entityHit, " +
                "hitUnloadedChunk=$hitUnloadedChunk, " +
                "hitMaxDistance=$hitMaxDistance, " +
                "hitPosition=$hitPosition, " +
                "relativeHitPosition=$relativeHitPosition, " +
                "hitFace=$hitFace" +
                "]"
    }
}