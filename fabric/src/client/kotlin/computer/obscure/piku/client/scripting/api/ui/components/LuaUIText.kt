package computer.obscure.piku.client.scripting.api.ui.components

import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.piku.common.ui.components.Text

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