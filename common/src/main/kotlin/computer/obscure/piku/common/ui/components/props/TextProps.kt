package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.classes.DirtyFlag
import computer.obscure.piku.common.ui.classes.UIColor

open class TextProps : BaseProps() {
    var text: String = ""
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.LAYOUT)
                mark(DirtyFlag.VISUAL)
            }
        }

    var color: UIColor = UIColor(255, 255, 255)
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }

    var shadow: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }

    var backgroundColor: UIColor? = null
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }

    var textScale: Vec2 = Vec2(1.0, 1.0)
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.LAYOUT)
                mark(DirtyFlag.TRANSFORM)
            }
        }

    var backgroundScale: Vec2 = Vec2(1.0, 1.0)
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.TRANSFORM)
            }
        }
}
