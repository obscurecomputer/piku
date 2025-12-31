package me.znotchill.piku.client.scripting.api.ui.components

import dev.znci.twine.annotations.TwineNativeProperty
import me.znotchill.piku.common.ui.components.Text

class LuaUIText(
    override val component: Text
) : LuaUIComponent(component) {
    @TwineNativeProperty
    var text: String
        get() = component.props.text
        set(value) {
            component.props.text = value
        }
}