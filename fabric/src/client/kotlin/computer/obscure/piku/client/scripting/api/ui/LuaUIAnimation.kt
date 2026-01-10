package computer.obscure.piku.client.scripting.api.ui

import computer.obscure.piku.client.scripting.engine.EngineError
import computer.obscure.piku.client.scripting.engine.EngineErrorCode
import computer.obscure.piku.common.scripting.api.LuaSpacingInstance
import computer.obscure.piku.common.scripting.api.LuaVec2Instance
import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.components.ProgressBar
import computer.obscure.piku.common.ui.components.move
import computer.obscure.piku.common.ui.components.opacity
import computer.obscure.piku.common.ui.components.padding
import computer.obscure.piku.common.ui.components.progress
import computer.obscure.piku.common.ui.components.rotate
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative

class LuaUIAnimation(
    val component: Component
) : TwineNative() {
    private fun invalidComponent(
        function: String,
        expected: String
    ): Nothing {
        throw EngineError(
            EngineErrorCode.INVALID_COMPONENT,
            "$function() is only supported on $expected components " +
                    "(got ${component.javaClass.simpleName}, id=${component.internalId})"
        )
    }

    @TwineNativeFunction
    fun move(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        component.move(
            to.toVec2(),
            duration,
            easing
        )
        return this
    }

    @TwineNativeFunction
    fun rotate(to: Int, duration: Double, easing: String): LuaUIAnimation {
        component.rotate(
            to,
            duration,
            easing
        )
        return this
    }

    @TwineNativeFunction
    fun opacity(to: Float, duration: Double, easing: String): LuaUIAnimation {
        component.opacity(to, duration, easing)
        return this
    }

    @TwineNativeFunction
    fun padding(to: LuaSpacingInstance, duration: Double, easing: String): LuaUIAnimation {
        component.padding(
            to.toSpacing(),
            duration,
            easing
        )
        return this
    }

    @TwineNativeFunction
    fun progress(to: Float, duration: Double, easing: String): LuaUIAnimation {
        if (component !is ProgressBar)
            invalidComponent("progress", "ProgressBar")

        component.progress(
            to,
            duration,
            easing
        )
        return this
    }
}