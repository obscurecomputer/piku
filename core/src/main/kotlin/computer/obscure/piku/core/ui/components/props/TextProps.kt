package computer.obscure.piku.core.ui.components.props

import computer.obscure.piku.core.classes.Vec2
import computer.obscure.piku.core.ui.classes.DirtyFlag
import computer.obscure.piku.core.ui.classes.UIColor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

open class TextProps : BaseProps() {
    var text: Component = Component.empty()
        set(value) {
            if (field !== value) {
                field = value
                rawText = PlainTextComponentSerializer.plainText().serialize(value)
                mark(DirtyFlag.LAYOUT)
            }
        }

    var rawText: String = ""
        private set

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
