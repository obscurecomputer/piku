package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.Anchor
import computer.obscure.piku.common.ui.classes.BorderRules
import computer.obscure.piku.common.ui.classes.DirtyFlag
import computer.obscure.piku.common.ui.classes.DirtyProps
import computer.obscure.piku.common.ui.classes.Spacing

open class BaseProps : DirtyProps() {
    var pos: Vec2 = Vec2(0.0, 0.0)
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.LAYOUT)
            }
        }

    var size: Vec2 = Vec2(0.0, 0.0)
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.LAYOUT)
            }
        }

    var anchor: Anchor = Anchor.CENTER_CENTER
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.LAYOUT)
            }
        }

    var padding: Spacing = Spacing(0.0)
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.LAYOUT)
            }
        }

    var margin: Spacing = Spacing(0.0)
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.LAYOUT)
            }
        }

    var scale: Vec2 = Vec2(1.0, 1.0)
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.TRANSFORM)
            }
        }

    var rotation: Int = 0
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.TRANSFORM)
            }
        }

    var opacity: Float = 1f
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }

    var visible: Boolean = true
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }

    var zIndex: Int = 0
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.ORDER)
            }
        }

    var border: BorderRules = BorderRules()
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }
}