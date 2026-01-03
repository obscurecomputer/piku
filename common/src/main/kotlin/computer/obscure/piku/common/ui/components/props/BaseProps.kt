package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.Anchor
import computer.obscure.piku.common.ui.classes.BorderRules
import computer.obscure.piku.common.ui.classes.Spacing

open class BaseProps(
    var pos: Vec2 = Vec2(0f, 0f),
    var size: Vec2 = Vec2(0f, 0f),
    var anchor: Anchor = Anchor.CENTER_CENTER,
    var rotation: Int = 0,
    var opacity: Float = 1f,
    var visible: Boolean = true,
    var zIndex: Int = 0,
    var padding: Spacing = Spacing(0f),
    var margin: Spacing = Spacing(0f),
    var scale: Vec2 = Vec2(1f, 1f),
    var border: BorderRules = BorderRules()
)