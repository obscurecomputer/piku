package computer.obscure.piku.core.ui.components

import computer.obscure.piku.core.ui.classes.CompType
import computer.obscure.piku.core.ui.components.props.BoxProps

open class Box(
    override val props: BoxProps
) : Component() {
    override val compType: CompType = CompType.BOX

    override fun width(): Double {
        return props.size.x
    }

    override fun height(): Double {
        return props.size.y
    }
}