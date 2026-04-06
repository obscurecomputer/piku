package computer.obscure.piku.mod.fabric.scripting.api.ui.components

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
import computer.obscure.piku.core.ui.UIEventQueue
import computer.obscure.piku.core.ui.classes.RelativePosition
import computer.obscure.piku.core.ui.components.*
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUIAnimation
import computer.obscure.piku.mod.fabric.ui.UIRenderer

open class LuaUIComponent(open val component: Component) : TwineNative() {
    @TwineProperty
    val id: String
        get() = component.internalId

    @TwineProperty
    var size: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.size)
        set(value) {
            println(value.toString())
            println(value.toVec2())
            component.props.size = value.toVec2()
        }

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
        get() = UIRenderer.animations().find { it.targetId == component.internalId } != null

    @TwineFunction
    fun cancelAnimations() {
        UIEventQueue.clear()
        UIRenderer.cancelAnimations()
    }

    @TwineFunction
    fun remove() {
        UIRenderer.currentWindow.components.remove(this.component.internalId)
    }
}
