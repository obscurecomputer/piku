package computer.obscure.piku.client.scripting.api.ui.components

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.piku.common.scripting.api.LuaVec2
import computer.obscure.piku.common.scripting.api.LuaVec2Instance
import computer.obscure.piku.common.ui.UIEventQueue
import computer.obscure.piku.common.ui.classes.RelativePosition
import computer.obscure.piku.common.ui.components.*
import computer.obscure.piku.common.ui.events.DestroyEvent
import computer.obscure.twine.annotations.TwineOverload

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

    @TwineNativeFunction
    fun rightOf(other: LuaUIComponent) {
        component.relativeTo = other.id
        component.relativePosition = RelativePosition.RIGHT_OF
    }

    @TwineNativeFunction
    fun leftOf(relativeComponent: LuaUIComponent) {
        relativeComponent.component.leftOf(component)
    }

    @TwineNativeFunction
    fun topOf(relativeComponent: LuaUIComponent) {
        relativeComponent.component.topOf(component)
    }

    @TwineNativeFunction
    fun bottomOf(relativeComponent: LuaUIComponent) {
        relativeComponent.component.bottomOf(component)
    }

//    TODO: fix bug in twine relating to overloads and properties
//    @TwineOverload
//    @TwineNativeFunction
//    fun move(to: LuaVec2Instance, duration: Double, easing: LuaEasingInstance) {
//        component.move(
//            to.toVec2(),
//            duration,
//            easing.id
//        )
//    }

    @TwineOverload
    @TwineNativeFunction
    fun move(to: LuaVec2Instance, duration: Double, easing: String) {
        component.move(
            to.toVec2(),
            duration,
            easing
        )
    }

    @TwineNativeFunction
    fun remove() {
        UIEventQueue.enqueueNow(
            DestroyEvent(
                delay = 0L,
                targetId = component.internalId
            )
        )
    }
}
