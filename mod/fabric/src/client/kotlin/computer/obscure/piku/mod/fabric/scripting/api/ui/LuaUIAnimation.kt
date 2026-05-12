package computer.obscure.piku.mod.fabric.scripting.api.ui

import computer.obscure.piku.core.animation.Animation
import computer.obscure.piku.core.scripting.api.LuaSpacingInstance
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.scripting.engine.EngineError
import computer.obscure.piku.core.scripting.engine.EngineErrorCode
import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.components.Line
import computer.obscure.piku.core.ui.components.ProgressBar
import computer.obscure.piku.mod.fabric.scripting.api.animation.LuaAnimatable
import computer.obscure.twine.LuaCallback
import computer.obscure.twine.annotations.TwineFunction

class LuaUIAnimation(val component: Component) : LuaAnimatable() {
    private var onStartCallback: LuaCallback? = null
    private var onFinishCallback: LuaCallback? = null

    private fun invalidComponent(function: String, expected: String): Nothing {
        throw EngineError(
            EngineErrorCode.INVALID_COMPONENT,
            "$function() is only supported on $expected components " +
                    "(got ${component.javaClass.simpleName}, id=${component.internalId})"
        )
    }

    @TwineFunction
    fun onStart(callback: LuaCallback): LuaUIAnimation {
        onStartCallback = callback
        return this
    }

    @TwineFunction
    fun onFinish(callback: LuaCallback): LuaUIAnimation {
        onFinishCallback = callback
        return this
    }

    @TwineFunction
    fun move(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        queue.add(Animation(
            targetId = component.internalId,
            durationSeconds = duration,
            easing = easing,
            getter = { component.props.pos },
            setter = { component.props.pos = it },
            to = to.toVec2(),
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun opacity(to: Float, duration: Double, easing: String): LuaUIAnimation {
        queue.add(Animation(
            targetId = component.internalId,
            durationSeconds = duration,
            easing = easing,
            getter = { component.props.opacity },
            setter = { component.props.opacity = it },
            to = to,
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun size(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        queue.add(Animation(
            targetId = component.internalId,
            durationSeconds = duration,
            easing = easing,
            getter = { component.props.size },
            setter = { component.props.size = it },
            to = to.toVec2(),
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun scale(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        queue.add(Animation(
            targetId = component.internalId,
            durationSeconds = duration,
            easing = easing,
            getter = { component.props.scale },
            setter = { component.props.scale = it },
            to = to.toVec2(),
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun rotate(to: Float, duration: Double, easing: String): LuaUIAnimation {
        queue.add(Animation(
            targetId = component.internalId,
            durationSeconds = duration,
            easing = easing,
            getter = { component.props.rotation },
            setter = { component.props.rotation = it },
            to = to,
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun padding(to: LuaSpacingInstance, duration: Double, easing: String): LuaUIAnimation {
        queue.add(Animation(
            targetId = component.internalId,
            durationSeconds = duration,
            easing = easing,
            getter = { component.props.padding },
            setter = { component.props.padding = it },
            to = to.toSpacing(),
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun progress(to: Float, duration: Double, easing: String): LuaUIAnimation {
        if (component !is ProgressBar)
            invalidComponent("progress", "ProgressBar")
        queue.add(Animation(
            targetId = component.internalId,
            durationSeconds = duration,
            easing = easing,
            getter = { component.props.progress },
            setter = { component.props.progress = it },
            to = to,
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun to(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        if (component !is Line)
            invalidComponent("to", "Line")
        queue.add(Animation(
            targetId = component.internalId,
            durationSeconds = duration,
            easing = easing,
            getter = { component.props.to },
            setter = { component.props.to = it },
            to = to.toVec2(),
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun from(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        if (component !is Line)
            invalidComponent("from", "Line")
        queue.add(Animation(
            targetId = component.internalId,
            durationSeconds = duration,
            easing = easing,
            getter = { component.props.from },
            setter = { component.props.from = it },
            to = to.toVec2(),
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }
}