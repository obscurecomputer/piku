package computer.obscure.piku.mod.fabric.scripting.api.ui

import computer.obscure.piku.core.animation.Animation
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.scripting.engine.EngineError
import computer.obscure.piku.core.scripting.engine.EngineErrorCode
import computer.obscure.piku.mod.fabric.scripting.api.animation.LuaAnimatable
import computer.obscure.piku.mod.fabric.ui.classes.Dimension
import computer.obscure.piku.mod.fabric.ui.components.FlowNode
import computer.obscure.piku.mod.fabric.ui.components.LineNode
import computer.obscure.piku.mod.fabric.ui.components.ProgressBarNode
import computer.obscure.piku.mod.fabric.ui.components.UINode
import computer.obscure.twine.LuaCallback
import computer.obscure.twine.annotations.TwineFunction

class LuaUIAnimation(val node: UINode) : LuaAnimatable() {
    private var onStartCallback: LuaCallback? = null
    private var onFinishCallback: LuaCallback? = null

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
    fun offset(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        queue.add(Animation(
            targetId = node.id,
            durationSeconds = duration,
            easing = easing,
            getter = { node.offset },
            setter = { node.offset = it },
            to = to.toVec2(),
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun opacity(to: Float, duration: Double, easing: String): LuaUIAnimation {
        queue.add(Animation(
            targetId = node.id,
            durationSeconds = duration,
            easing = easing,
            getter = { node.opacity },
            setter = { node.opacity = it },
            to = to,
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun width(to: Float, duration: Double, easing: String): LuaUIAnimation {
        queue.add(Animation(
            targetId = node.id,
            durationSeconds = duration,
            easing = easing,
            getter = { (node.width as? Dimension.Fixed)?.px ?: node.measuredWidth },
            setter = { node.width = Dimension.Fixed(it) },
            to = to,
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun height(to: Float, duration: Double, easing: String): LuaUIAnimation {
        queue.add(Animation(
            targetId = node.id,
            durationSeconds = duration,
            easing = easing,
            getter = { (node.height as? Dimension.Fixed)?.px ?: node.measuredHeight },
            setter = { node.height = Dimension.Fixed(it) },
            to = to,
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun progress(to: Float, duration: Double, easing: String): LuaUIAnimation {
        if (node !is ProgressBarNode)
            throw EngineError(EngineErrorCode.INVALID_COMPONENT,
                "progress() is only supported on ProgressBar nodes")
        queue.add(Animation(
            targetId = node.id,
            durationSeconds = duration,
            easing = easing,
            getter = { node.value },
            setter = { node.value = it },
            to = to,
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun scroll(to: Float, duration: Double, easing: String): LuaUIAnimation {
        if (node !is FlowNode)
            throw EngineError(EngineErrorCode.INVALID_COMPONENT,
                "scroll() is only supported on Row/Column nodes")
        queue.add(Animation(
            targetId = node.id,
            durationSeconds = duration,
            easing = easing,
            getter = { node.scrollOffset },
            setter = { node.scrollOffset = it },
            to = -to, // negative because scroll is inverted internally
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }

    @TwineFunction
    fun to(to: LuaVec2Instance, duration: Double, easing: String): LuaUIAnimation {
        if (node !is LineNode)
            throw EngineError(EngineErrorCode.INVALID_COMPONENT,
                "to() is only supported on Line nodes")
        queue.add(Animation(
            targetId = node.id,
            durationSeconds = duration,
            easing = easing,
            getter = { node.to },
            setter = { node.to = it },
            to = to.toVec2(),
            onStart = { onStartCallback?.call<Unit>() },
            onFinish = { onFinishCallback?.call<Unit>() }
        ))
        return this
    }
}