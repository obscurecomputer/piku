package me.znotchill.piku.client.scripting.api.ui.components

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import me.znotchill.piku.common.scripting.api.LuaVec2
import me.znotchill.piku.common.scripting.api.LuaVec2Instance
import me.znotchill.piku.common.ui.components.*

open class LuaUIComponent(open val component: Component) : TwineNative() {
    @TwineNativeProperty
    var size: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.size)
        set(value) {
            component.props.size = value.toVec2()
        }

    @TwineNativeFunction
    fun rightOf(relativeComponent: LuaUIComponent) {
        relativeComponent.component.rightOf(component)
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
}
