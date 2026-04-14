package computer.obscure.piku.mod.fabric.scripting.api.raycast

import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.piku.mod.fabric.raycast.Raycast
import computer.obscure.piku.mod.fabric.scripting.PikuError
import computer.obscure.piku.mod.fabric.scripting.api.LuaEntity
import computer.obscure.piku.mod.fabric.scripting.api.util.minecraft.toMCVec3
import computer.obscure.twine.LuaCallback
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

class LuaRaycast : TwineNative("raycast") {
    @TwineFunction
    fun new(): LuaRaycastBuilder {
        return LuaRaycastBuilder()
    }
}

class LuaRaycastBuilder : TwineNative() {
    var start: Vec3 = Vec3.ZERO
    var direction: Vec3 = Vec3.ZERO
    var maxDistance: Double = 200.0
    var nearbyEntityRadius: Double = 3.0
    var entityFilter: (Entity) -> Boolean = { true }

    @TwineFunction
    fun start(vec3: LuaVec3Instance): LuaRaycastBuilder {
        this.start = vec3.toMCVec3()
        return this
    }

    @TwineFunction
    fun direction(vec3: LuaVec3Instance): LuaRaycastBuilder {
        this.direction = vec3.toMCVec3()
        return this
    }

    @TwineFunction
    fun maxDistance(value: Double): LuaRaycastBuilder {
        if (value < 0) return this
        this.maxDistance = value
        return this
    }

    @TwineFunction
    fun nearbyEntityRadius(value: Double): LuaRaycastBuilder {
        if (value < 0) return this
        this.nearbyEntityRadius = value
        return this
    }

    @TwineFunction
    fun entityFilter(value: LuaCallback): LuaRaycastBuilder {
        this.entityFilter = { entity ->
            val results = value.call(LuaEntity(entity))
            if (results.size != 1) throw PikuError("entityFilter return size should be 1")
            val result = results.first()!!
            if (result !is Boolean) throw PikuError("entityFilter should return a boolean")
            result
        }
        return this
    }

    @TwineFunction
    fun shoot(): LuaRaycastResult {
        val instance = Minecraft.getInstance()
        if (instance.level == null) return Raycast.createEmptyResult().toLua()

        return LuaRaycastResult(
            Raycast.shoot(
                instance.level!!,
                start,
                direction,
                maxDistance,
                nearbyEntityRadius,
                entityFilter
            )
        )
    }
}