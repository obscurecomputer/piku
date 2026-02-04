package computer.obscure.piku.core.ui.components

import computer.obscure.piku.core.ui.classes.CompType
import computer.obscure.piku.core.ui.components.props.GradientProps

open class Gradient(
    override val props: GradientProps
) : Component() {
    override val compType: CompType = CompType.GRADIENT

    override fun width(): Double {
        return 1.0
    }

    override fun height(): Double {
        return 1.0
    }
}