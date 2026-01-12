package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.Anchor
import computer.obscure.piku.common.ui.classes.BorderRules
import computer.obscure.piku.common.ui.classes.Spacing

open class BaseProps(
    var pos: Vec2 = Vec2(0.0, 0.0),
    var size: Vec2 = Vec2(0.0, 0.0),
    var anchor: Anchor = Anchor.CENTER_CENTER,
    var rotation: Int = 0,
    var opacity: Float = 1f,
    var visible: Boolean = true,
    var zIndex: Int = 0,
    var padding: Spacing = Spacing(0.0),
    var margin: Spacing = Spacing(0.0),
    var scale: Vec2 = Vec2(1.0, 1.0),
    var border: BorderRules = BorderRules()
)