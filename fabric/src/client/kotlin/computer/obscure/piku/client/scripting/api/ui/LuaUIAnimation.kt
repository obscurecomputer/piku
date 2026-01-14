package computer.obscure.piku.client.scripting.api.ui

import computer.obscure.piku.common.scripting.api.LuaSpacingInstance
import computer.obscure.piku.common.scripting.api.LuaVec2Instance
import computer.obscure.piku.common.scripting.engine.EngineError
import computer.obscure.piku.common.scripting.engine.EngineErrorCode
import computer.obscure.piku.common.ui.UIEventQueue
import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.components.Line
import computer.obscure.piku.common.ui.components.ProgressBar
import computer.obscure.piku.common.ui.events.*
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative

class LuaUIAnimation(
    val component: Component
) : TwineNative() {
    val storedEvents: MutableList<UIEvent> = mutableListOf()

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
    fun play() {
        storedEvents.forEach {
            UIEventQueue.enqueueNow(it)
        }
    }

    @TwineNativeFunction
    fun move(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        storedEvents.add(
            MoveEvent(
                targetId = component.internalId,
                delay = 0,
                position = to.toVec2(),
                durationSeconds = duration,
                easing = easing
            )
        )

        return this
    }

    @TwineNativeFunction
    fun size(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        storedEvents.add(
            SizeEvent(
                targetId = component.internalId,
                delay = 0,
                size = to.toVec2(),
                durationSeconds = duration,
                easing = easing
            )
        )

        return this
    }

    @TwineNativeFunction
    fun scale(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        storedEvents.add(
            ScaleEvent(
                targetId = component.internalId,
                delay = 0,
                scale = to.toVec2(),
                durationSeconds = duration,
                easing = easing
            )
        )

        return this
    }

    @TwineNativeFunction
    fun rotate(to: Int, duration: Double, easing: String): LuaUIAnimation {
        storedEvents.add(
            RotateEvent(
                targetId = component.internalId,
                delay = 0,
                rotation = to,
                durationSeconds = duration,
                easing = easing
            )
        )

        return this
    }

    @TwineNativeFunction
    fun opacity(to: Float, duration: Double, easing: String): LuaUIAnimation {
        storedEvents.add(
            OpacityEvent(
                targetId = component.internalId,
                delay = 0,
                opacity = to,
                durationSeconds = duration,
                easing = easing
            )
        )

        return this
    }

    @TwineNativeFunction
    fun padding(to: LuaSpacingInstance, duration: Double, easing: String): LuaUIAnimation {
        storedEvents.add(
            PaddingEvent(
                targetId = component.internalId,
                delay = 0,
                padding = to.toSpacing(),
                durationSeconds = duration,
                easing = easing
            )
        )

        return this
    }

    @TwineNativeFunction
    fun progress(to: Float, duration: Double, easing: String): LuaUIAnimation {
        if (component !is ProgressBar)
            invalidComponent("progress", "ProgressBar")

        storedEvents.add(
            ProgressEvent(
                targetId = component.internalId,
                delay = 0,
                progress = to,
                durationSeconds = duration,
                easing = easing
            )
        )

        return this
    }

    @TwineNativeFunction
    fun to(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        if (component !is Line)
            invalidComponent("to", "Line")

        storedEvents.add(
            LineToEvent(
                targetId = component.internalId,
                delay = 0,
                position = to.toVec2(),
                durationSeconds = duration,
                easing = easing
            )
        )

        return this
    }

    @TwineNativeFunction
    fun from(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        if (component !is Line)
            invalidComponent("from", "Line")

        storedEvents.add(
            LineFromEvent(
                targetId = component.internalId,
                delay = 0,
                position = to.toVec2(),
                durationSeconds = duration,
                easing = easing
            )
        )

        return this
    }
}