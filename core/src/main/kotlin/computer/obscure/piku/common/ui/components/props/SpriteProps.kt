package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.ui.classes.DirtyFlag

open class SpriteProps : BaseProps() {

    var texturePath: String = ""
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.LAYOUT)
                mark(DirtyFlag.VISUAL)
            }
        }

    var fillScreen: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.LAYOUT)
            }
        }
}
