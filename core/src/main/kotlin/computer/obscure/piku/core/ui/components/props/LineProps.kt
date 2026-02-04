package computer.obscure.piku.core.ui.components.props

import computer.obscure.piku.core.classes.Vec2
import computer.obscure.piku.core.ui.classes.DirtyFlag
import computer.obscure.piku.core.ui.classes.UIColor

open class LineProps : BaseProps() {
    var geometryDirty: Boolean = false

    var from: Vec2 = Vec2(0.0, 0.0)
        set(value) {
            if (field != value) {
                field = value
                geometryDirty = true
            }
        }

    var to: Vec2 = Vec2(0.0, 0.0)
        set(value) {
            if (field != value) {
                field = value
                geometryDirty = true
            }
        }

    var color: UIColor = UIColor(255, 255, 255)
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }

    var pointSize: Vec2 = Vec2(2.0, 2.0)
        set(value) {
            if (field != value) {
                field = value
                geometryDirty = true
            }
        }
}
