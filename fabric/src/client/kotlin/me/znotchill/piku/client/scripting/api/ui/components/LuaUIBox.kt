package me.znotchill.piku.client.scripting.api.ui.components

import dev.znci.twine.annotations.TwineNativeProperty
import me.znotchill.piku.common.scripting.api.LuaColor
import me.znotchill.piku.common.scripting.api.LuaColorInstance
import me.znotchill.piku.common.ui.components.Box

class LuaUIBox(
    override val component: Box
) : LuaUIComponent(component) {
    @TwineNativeProperty
    var color: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.color)
        set(value) {
            component.props.color = value.toUIColor()
        }
}