package computer.obscure.piku.core.scripting.api

import computer.obscure.piku.core.noise.FastNoiseLite
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction

class LuaNoise : TwineNative("noise") {
    @TwineFunction
    fun new(seed: Int): LuaNoiseBuilder {
        return LuaNoiseBuilder(seed)
    }
}

class LuaNoiseBuilder(val seed: Int) : TwineNative() {
    private val internalNoise = FastNoiseLite()

    init {
        seed(seed)
    }

    @TwineFunction
    fun seed(seed: Int): LuaNoiseBuilder {
        internalNoise.SetSeed(seed)
        return this
    }

    @TwineFunction
    fun noiseType(type: String): LuaNoiseBuilder {
        when (type.lowercase()) {
            "perlin" -> internalNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin)
            "opensimplex2" -> internalNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2)
            "cellular" -> internalNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular)
            "value" -> internalNoise.SetNoiseType(FastNoiseLite.NoiseType.Value)
        }
        return this
    }

    @TwineFunction
    fun get(x: Double, y: Double): Double {
        return internalNoise.GetNoise(x.toFloat(), y.toFloat()).toDouble()
    }

    @TwineFunction
    fun get(vec2: LuaVec2Instance): Double {
        return internalNoise.GetNoise(vec2.x.toFloat(), vec2.y.toFloat()).toDouble()
    }

    @TwineFunction
    fun frequency(freq: Double): LuaNoiseBuilder {
        internalNoise.SetFrequency(freq.toFloat())
        return this
    }
}