package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.classes.DirtyFlag
import computer.obscure.piku.common.ui.classes.UIColor

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
