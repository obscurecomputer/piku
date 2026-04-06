package computer.obscure.piku.mod.fabric.scripting.api.ui

import computer.obscure.piku.core.scripting.api.LuaSpacingInstance
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.scripting.engine.EngineError
import computer.obscure.piku.core.scripting.engine.EngineErrorCode
import computer.obscure.piku.core.ui.UIEventQueue
import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.components.Line
import computer.obscure.piku.core.ui.components.ProgressBar
import computer.obscure.piku.core.ui.events.*
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative

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

    @TwineFunction
    fun play() {
        storedEvents.forEach {
            UIEventQueue.enqueueNow(it)
        }
    }

    @TwineFunction
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

    @TwineFunction
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

    @TwineFunction
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

    @TwineFunction
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

    @TwineFunction
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

    @TwineFunction
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

    @TwineFunction
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

    @TwineFunction
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

    @TwineFunction
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