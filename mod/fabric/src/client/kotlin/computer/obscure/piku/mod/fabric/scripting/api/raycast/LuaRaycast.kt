package computer.obscure.piku.mod.fabric.scripting.api.raycast

import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.piku.mod.fabric.raycast.Raycast
import computer.obscure.piku.mod.fabric.scripting.api.util.minecraft.toMCVec3
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import net.minecraft.client.Minecraft
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
    var travelStep: Double = 0.5
    var nearbyEntityRadius: Double = 3.0

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
    fun travelStep(value: Double): LuaRaycastBuilder {
        if (value < 0) return this
        this.travelStep = value
        return this
    }

    @TwineFunction
    fun nearbyEntityRadius(value: Double): LuaRaycastBuilder {
        if (value < 0) return this
        this.nearbyEntityRadius = value
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
                travelStep,
                nearbyEntityRadius
            )
        )
    }
}