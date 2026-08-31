package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.mod.fabric.ui.classes.Spacing
import computer.obscure.piku.mod.fabric.animation.Animation
import computer.obscure.piku.mod.fabric.animation.AnimationManager
import computer.obscure.piku.mod.fabric.ui.classes.Dimension
import computer.obscure.piku.mod.fabric.ui.classes.OffsetDimension
import computer.obscure.piku.mod.fabric.ui.classes.ScaleDimension2
import computer.obscure.piku.mod.fabric.ui.components.FlowNode
import computer.obscure.piku.mod.fabric.ui.components.LineNode
import computer.obscure.piku.mod.fabric.ui.components.ProgressBarNode
import computer.obscure.piku.mod.fabric.ui.components.TextNode
import computer.obscure.piku.mod.fabric.ui.components.UINode
import computer.obscure.piku.mod.fabric.utils.parseScaleRegex
import me.znotchill.kiwi.generated.Color
import me.znotchill.kiwi.generated.Vec2

class UINodeAnim(val node: UINode) {
    val queue: MutableList<Animation<*>> = mutableListOf()

    fun onStart(callback: () -> Unit) = apply {
        onStartCallback = callback
    }

    fun onFinish(callback: () -> Unit) = apply {
        onFinishCallback = callback
    }

    fun play() {
        queue.forEach { AnimationManager.animate(it) }
    }

    fun offset(
        to: Vec2,
        duration: Double,
        easing: String,
        onStart: () -> Unit = {},
        onFinish: () -> Unit = {}
    ) = apply {
        queue.add(
            Animation(
                targetId = node.id,
                durationSeconds = duration,
                easing = easing,
                getter = {
                    Vec2(
                        (node.offsetX as? OffsetDimension.Fixed)?.px ?: 0f,
                        (node.offsetY as? OffsetDimension.Fixed)?.px ?: 0f
                    )
                },
                setter = { offset ->
                    node.offsetX = OffsetDimension.Fixed(offset.x.toFloat())
                    node.offsetY = OffsetDimension.Fixed(offset.y.toFloat())
                },
                to = to,
                onStart = {
                    onStartCallback?.invoke()
                    onStart()
                },
                onFinish = {
                    onFinishCallback?.invoke()
                    onFinish()
                }
            )
        )
    }

    fun opacity(
        to: Float,
        duration: Double,
        easing: String,
        onStart: () -> Unit = {},
        onFinish: () -> Unit = {}
    ) = apply {
        queue.add(
            Animation(
                targetId = node.id,
                durationSeconds = duration,
                easing = easing,
                getter = { node.opacity },
                setter = { node.opacity = it },
                to = to,
                onStart = {
                    onStartCallback?.invoke()
                    onStart()
                },
                onFinish = {
                    onFinishCallback?.invoke()
                    onFinish()
                }
            )
        )
    }

    fun width(
        to: Float,
        duration: Double,
        easing: String,
        onStart: () -> Unit = {},
        onFinish: () -> Unit = {}
    ) = apply {
        queue.add(
            Animation(
                targetId = node.id,
                durationSeconds = duration,
                easing = easing,
                getter = { (node.width as? Dimension.Fixed)?.px ?: node.measuredWidth },
                setter = { node.width = Dimension.Fixed(it) },
                to = to,
                onStart = {
                    onStartCallback?.invoke()
                    onStart()
                },
                onFinish = {
                    onFinishCallback?.invoke()
                    onFinish()
                }
            )
        )
    }

    fun height(
        to: Float,
        duration: Double,
        easing: String,
        onStart: () -> Unit = {},
        onFinish: () -> Unit = {}
    ) = apply {
        queue.add(
            Animation(
                targetId = node.id,
                durationSeconds = duration,
                easing = easing,
                getter = { (node.height as? Dimension.Fixed)?.px ?: node.measuredHeight },
                setter = { node.height = Dimension.Fixed(it) },
                to = to,
                onStart = {
                    onStartCallback?.invoke()
                    onStart()
                },
                onFinish = {
                    onFinishCallback?.invoke()
                    onFinish()
                }
            )
        )
    }

    fun size(
        to: Vec2,
        duration: Double,
        easing: String,
        onStart: () -> Unit = {},
        onFinish: () -> Unit = {}
    ) = apply {
        queue.add(
            Animation(
                targetId = node.id,
                durationSeconds = duration,
                easing = easing,
                getter = {
                    Vec2(
                        (node.width as? Dimension.Fixed)?.px ?: node.measuredWidth,
                        (node.height as? Dimension.Fixed)?.px ?: node.measuredHeight
                    )
                },
                setter = { size ->
                    node.width = Dimension.Fixed(size.x.toFloat())
                    node.height = Dimension.Fixed(size.y.toFloat())
                },
                to = to,
                onStart = {
                    onStartCallback?.invoke()
                    onStart()
                },
                onFinish = {
                    onFinishCallback?.invoke()
                    onFinish()
                }
            )
        )
    }

    fun scale(
        to: String,
        duration: Double,
        easing: String,
        onStart: () -> Unit = {},
        onFinish: () -> Unit = {}
    ) = apply {
        val target = parseScaleRegex(to)

        check(node is TextNode) {
            "scale() is only supported on Text nodes"
        }

        queue.add(
            Animation(
                targetId = node.id,
                durationSeconds = duration,
                easing = easing,
                getter = {
                    ScaleDimension2(
                        x = node.scaleX,
                        y = node.scaleY
                    )
                },
                setter = { scale ->
                    node.scaleX = scale.x
                    node.scaleY = scale.y
                },
                to = target,
                onStart = {
                    onStartCallback?.invoke()
                    onStart()
                },
                onFinish = {
                    onFinishCallback?.invoke()
                    onFinish()
                }
            )
        )
    }

    fun progress(
        to: Float,
        duration: Double,
        easing: String,
        onStart: () -> Unit = {},
        onFinish: () -> Unit = {}
    ) = apply {
        check(node is ProgressBarNode) {
            "progress() is only supported on ProgressBar nodes"
        }

        queue.add(
            Animation(
                targetId = node.id,
                durationSeconds = duration,
                easing = easing,
                getter = { node.value },
                setter = { node.value = it },
                to = to,
                onStart = {
                    onStartCallback?.invoke()
                    onStart()
                },
                onFinish = {
                    onFinishCallback?.invoke()
                    onFinish()
                }
            )
        )
    }

    fun scroll(
        to: Float,
        duration: Double,
        easing: String,
        onStart: () -> Unit = {},
        onFinish: () -> Unit = {}
    ) = apply {
        check(node is FlowNode) {
            "scroll() is only supported on Row/Column nodes"
        }

        queue.add(
            Animation(
                targetId = node.id,
                durationSeconds = duration,
                easing = easing,
                getter = { node.scrollOffset },
                setter = { node.scrollOffset = it },
                to = -to,
                onStart = {
                    onStartCallback?.invoke()
                    onStart()
                },
                onFinish = {
                    onFinishCallback?.invoke()
                    onFinish()
                }
            )
        )
    }

    fun to(
        to: Vec2,
        duration: Double,
        easing: String,
        onStart: () -> Unit = {},
        onFinish: () -> Unit = {}
    ) = apply {
        check(node is LineNode) {
            "to() is only supported on Line nodes"
        }

        queue.add(
            Animation(
                targetId = node.id,
                durationSeconds = duration,
                easing = easing,
                getter = { node.to },
                setter = { node.to = it },
                to = to,
                onStart = {
                    onStartCallback?.invoke()
                    onStart()
                },
                onFinish = {
                    onFinishCallback?.invoke()
                    onFinish()
                }
            )
        )
    }

    fun background(
        to: Color,
        duration: Double,
        easing: String,
        onStart: () -> Unit = {},
        onFinish: () -> Unit = {}
    ) = apply {
        queue.add(
            Animation(
                targetId = node.id,
                durationSeconds = duration,
                easing = easing,
                getter = { node.background },
                setter = { node.background = it },
                to = to,
                onStart = {
                    onStartCallback?.invoke()
                    onStart()
                },
                onFinish = {
                    onFinishCallback?.invoke()
                    onFinish()
                }
            )
        )
    }

    fun color(
        to: Color,
        duration: Double,
        easing: String,
        onStart: () -> Unit = {},
        onFinish: () -> Unit = {}
    ) = apply {
        queue.add(
            Animation(
                targetId = node.id,
                durationSeconds = duration,
                easing = easing,
                getter = { node.color },
                setter = { node.color = it },
                to = to,
                onStart = {
                    onStartCallback?.invoke()
                    onStart()
                },
                onFinish = {
                    onFinishCallback?.invoke()
                    onFinish()
                }
            )
        )
    }

    fun padding(
        to: Spacing,
        duration: Double,
        easing: String,
        onStart: () -> Unit = {},
        onFinish: () -> Unit = {}
    ) = apply {
        queue.add(
            Animation(
                targetId = node.id,
                durationSeconds = duration,
                easing = easing,
                getter = { node.padding },
                setter = { node.padding = it },
                to = to,
                onStart = {
                    onStartCallback?.invoke()
                    onStart()
                },
                onFinish = {
                    onFinishCallback?.invoke()
                    onFinish()
                }
            )
        )
    }

    private var onStartCallback: (() -> Unit)? = null
    private var onFinishCallback: (() -> Unit)? = null
}
