package computer.obscure.piku.mod.common.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaSpacing
import computer.obscure.piku.core.scripting.api.LuaSpacingInstance
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.piku.core.scripting.api.LuaVec2
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.scripting.engine.EngineError
import computer.obscure.piku.core.scripting.engine.EngineErrorCode
import computer.obscure.piku.core.ui.Anchor
import computer.obscure.piku.core.ui.UIEventQueue
import computer.obscure.piku.core.ui.classes.RelativePosition
import computer.obscure.piku.core.ui.components.*
import computer.obscure.piku.mod.common.scripting.api.ui.LuaUIAnimation
import computer.obscure.piku.mod.common.ui.UIRenderer

open class LuaUIComponent(open val component: Component) : TwineNative() {
    @TwineNativeProperty
    val id: String
        get() = component.internalId

    @TwineNativeProperty
    var size: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.size)
        set(value) {
            component.props.size = value.toVec2()
        }

    @TwineNativeFunction
    fun size(value: LuaVec2Instance): LuaUIComponent {
        component.props.size = value.toVec2()
        return this
    }

    @TwineNativeProperty
    var opacity: Float
        get() = component.props.opacity
        set(value) {
            component.props.opacity = value
        }

    @TwineNativeFunction
    fun opacity(value: Float): LuaUIComponent {
        component.props.opacity = value
        return this
    }

    @TwineNativeProperty
    var pos: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.pos)
        set(value) {
            component.props.pos = value.toVec2()
        }

    @TwineNativeFunction
    fun pos(value: LuaVec2Instance): LuaUIComponent {
        component.props.pos = value.toVec2()
        return this
    }

    @TwineNativeProperty
    var padding: LuaSpacingInstance
        get() = LuaSpacing.fromSpacing(component.props.padding)
        set(value) {
            component.props.padding = value.toSpacing()
        }

    @TwineNativeFunction
    fun padding(value: LuaSpacingInstance): LuaUIComponent {
        component.props.padding = value.toSpacing()
//        println(value.toSpacing())
//        println(component.props.padding)
        return this
    }

    @TwineNativeFunction
    fun anchor(value: String): LuaUIComponent {
        component.props.anchor =
            Anchor.entries.find { it.name.equals(value, ignoreCase = true) }
                ?: throw EngineError(
                    EngineErrorCode.INVALID_ANCHOR,
                    "Unknown anchor \"$value\"."
                )

        return this
    }

    @TwineNativeProperty
    var scale: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.scale)
        set(value) {
            component.props.scale = value.toVec2()
        }

    @TwineNativeFunction
    fun scale(value: LuaVec2Instance): LuaUIComponent {
        component.props.scale = value.toVec2()
        return this
    }

    @TwineNativeFunction
    fun rightOf(otherId: String): LuaUIComponent {
        component.relativeTo = otherId
        component.relativePosition = RelativePosition.RIGHT_OF
        return this
    }

    @TwineNativeFunction
    fun leftOf(otherId: String): LuaUIComponent {
        component.relativeTo = otherId
        component.relativePosition = RelativePosition.LEFT_OF
        return this
    }

    @TwineNativeFunction
    fun topOf(otherId: String): LuaUIComponent {
        component.relativeTo = otherId
        component.relativePosition = RelativePosition.ABOVE
        return this
    }

    @TwineNativeFunction
    fun bottomOf(otherId: String): LuaUIComponent {
        component.relativeTo = otherId
        component.relativePosition = RelativePosition.BELOW
        return this
    }

    @TwineNativeFunction
    fun relative(otherId: String): LuaUIComponent {
        component.relativeTo = otherId
        return this
    }

    @TwineNativeFunction
    fun animate(): LuaUIAnimation {
        return LuaUIAnimation(this.component)
    }

    @TwineNativeFunction
    fun cancelAnimations() {
        UIEventQueue.clear()
        UIRenderer.cancelAnimations()
    }

    @TwineNativeFunction
    fun remove() {
        UIRenderer.currentWindow.components.remove(this.component.internalId)
    }
}
