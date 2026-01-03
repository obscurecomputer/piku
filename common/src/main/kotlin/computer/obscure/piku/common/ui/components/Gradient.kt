package computer.obscure.piku.common.ui.components

import computer.obscure.piku.common.ui.classes.CompType
import computer.obscure.piku.common.ui.components.props.GradientProps

open class Gradient(
    override val props: GradientProps
) : Component() {
    override val compType: CompType = CompType.GRADIENT

    override fun width(): Float {
        return 1f
    }

    override fun height(): Float {
        return 1f
    }
}