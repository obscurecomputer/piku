package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.animation.AnimationManager
import computer.obscure.piku.core.scripting.api.LuaSpacing
import computer.obscure.piku.core.scripting.api.LuaSpacingInstance
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative
import computer.obscure.piku.core.scripting.api.LuaVec2
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.scripting.engine.EngineError
import computer.obscure.piku.core.scripting.engine.EngineErrorCode
import computer.obscure.piku.core.ui.Anchor
import computer.obscure.piku.core.ui.classes.RelativePosition
import computer.obscure.piku.core.ui.components.*
import computer.obscure.piku.core.ui.components.props.TransitionProps
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUI
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUIAnimation
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import computer.obscure.twine.LuaCallback

open class LuaUIComponent(open val component: Component) : TwineNative() {
    @TwineProperty
    val id: String
        get() = component.internalId
    @TwineProperty
    val name: String
        get() = component.name

    @TwineFunction
    fun onLayout(callback: LuaCallback): LuaUIComponent {
        component.props.onLayout = { callback.call<Unit>() }
        return this
    }
    @TwineFunction
    fun beforeLayout(callback: LuaCallback): LuaUIComponent {
        component.props.beforeLayout = { callback.call<Unit>() }
        return this
    }
    @TwineFunction
    fun transition(duration: Double, easing: String): LuaUIComponent {
        component.props.transition = TransitionProps(duration, easing)
        return this
    }

    @TwineProperty
    var size: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.size)
        set(value) {
            component.props.size = value.toVec2()
        }

    @TwineProperty
    val computedSize: LuaVec2Instance
        get() = LuaVec2Instance(component.computedSize?.x ?: component.props.size.x,
            component.computedSize?.y ?: component.props.size.y)

    @TwineProperty
    val type: String
        get() = component.compType.name.lowercase()

    @TwineFunction
    fun size(value: LuaVec2Instance): LuaUIComponent {
        component.props.size = value.toVec2()
        return this
    }

    @TwineProperty
    var opacity: Float
        get() = component.props.opacity
        set(value) {
            component.props.opacity = value
        }

    @TwineFunction
    fun opacity(value: Float): LuaUIComponent {
        component.props.opacity = value
        return this
    }

    @TwineProperty
    var zIndex: Int
        get() = component.props.zIndex
        set(value) {
            component.props.zIndex = value
        }

    @TwineFunction
    fun zIndex(value: Int): LuaUIComponent {
        component.props.zIndex = value
        return this
    }

    @TwineProperty
    var position: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.pos)
        set(value) {
            component.props.pos = value.toVec2()
        }

    @TwineFunction
    fun pos(value: LuaVec2Instance): LuaUIComponent {
        component.props.pos = value.toVec2()
        return this
    }

    @TwineProperty
    var rotation: Float
        get() = component.props.rotation
        set(value) {
            component.props.rotation = component.props.rotation
        }

    @TwineFunction
    fun rotate(value: Float): LuaUIComponent {
        component.props.rotation = value
        return this
    }

    @TwineProperty
    var padding: LuaSpacingInstance
        get() = LuaSpacing.fromSpacing(component.props.padding)
        set(value) {
            component.props.padding = value.toSpacing()
        }

    @TwineFunction
    fun padding(value: LuaSpacingInstance): LuaUIComponent {
        component.props.padding = value.toSpacing()
//        println(value.toSpacing())
//        println(component.props.padding)
        return this
    }

    @TwineFunction
    fun anchor(value: String): LuaUIComponent {
        component.props.anchor =
            Anchor.entries.find { it.name.equals(value, ignoreCase = true) }
                ?: throw EngineError(
                    EngineErrorCode.INVALID_ANCHOR,
                    "Unknown anchor \"$value\"."
                )

        return this
    }

    @TwineProperty
    var scale: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.scale)
        set(value) {
            component.props.scale = value.toVec2()
        }

    @TwineFunction
    fun scale(value: LuaVec2Instance): LuaUIComponent {
        component.props.scale = value.toVec2()
        return this
    }

    @TwineFunction
    fun rightOf(otherId: String): LuaUIComponent {
        component.relativeTo = otherId
        component.relativePosition = RelativePosition.RIGHT_OF
        return this
    }

    @TwineFunction
    fun leftOf(otherId: String): LuaUIComponent {
        component.relativeTo = otherId
        component.relativePosition = RelativePosition.LEFT_OF
        return this
    }

    @TwineFunction
    fun topOf(otherId: String): LuaUIComponent {
        component.relativeTo = otherId
        component.relativePosition = RelativePosition.ABOVE
        return this
    }

    @TwineFunction
    fun bottomOf(otherId: String): LuaUIComponent {
        component.relativeTo = otherId
        component.relativePosition = RelativePosition.BELOW
        return this
    }

    @TwineFunction
    fun relative(otherId: String): LuaUIComponent {
        component.relativeTo = otherId
        return this
    }

    @TwineFunction
    fun animate(): LuaUIAnimation {
        return LuaUIAnimation(this.component)
    }

    @TwineProperty
    val isAnimating: Boolean
        get() = AnimationManager.isAnimating(component.internalId)

    @TwineFunction
    fun cancelAnimations() {
        AnimationManager.cancelFor(component.internalId)
    }

    @TwineFunction
    fun remove() {
        // try root window first
        if (UIRenderer.currentWindow.components.remove(this.component.internalId) != null) return

        // otherwise search through all containers
        UIRenderer.allComponents().forEach { parent ->
            when (parent) {
                is Group -> parent.props.components.removeIf { it.internalId == component.internalId }
                is FlowContainer -> parent.props.components.removeIf { it.internalId == component.internalId }
                else -> {}
            }
        }

        UIRenderer.currentWindow.unregisterRecursive(component.internalId)
        UIRenderer.layout(UIRenderer.currentWindow)
    }

    @TwineFunction
    fun contains(x: Float, y: Float): Boolean {
        val sx = component.screenX.toFloat()
        val sy = component.screenY.toFloat()
        return x >= sx && x <= sx + component.width() && y >= sy && y <= sy + component.height()
    }

    @TwineProperty
    val screenPos: LuaVec2Instance
        get() = LuaVec2Instance(component.screenX.toDouble(), component.screenY.toDouble())

    @TwineFunction
    fun getTouchingComponents(): List<LuaUIComponent> {
        val x = screenPos.x
        val y = screenPos.y
        return UIRenderer.allComponents()
            .filter { c ->
                x >= c.screenX && x <= c.screenX + c.width() &&
                        y >= c.screenY && y <= c.screenY + c.height()
            }
            .mapNotNull { LuaUI.wrap(it) }
    }
}
