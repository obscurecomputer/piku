package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos

class LuaLevel : TwineNative("level") {
    val instance: Minecraft = Minecraft.getInstance()

    @TwineFunction
    fun getBlock(vec3: LuaVec3Instance): LuaBlockState {
        val pos = BlockPos(vec3.x.toInt(), vec3.y.toInt(), vec3.z.toInt())
        val state = instance.level!!.getBlockState(pos)
        return LuaBlockState(state)
    }
}