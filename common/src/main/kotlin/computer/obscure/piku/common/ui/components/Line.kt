package computer.obscure.piku.common.ui.components

import computer.obscure.piku.common.ui.classes.CompType
import computer.obscure.piku.common.ui.components.props.LineProps

open class Line(
    override val props: LineProps
) : Component() {
    override val compType: CompType = CompType.LINE

    override fun width(): Double {
        return 1.0
    }

    override fun height(): Double {
        return 1.0
    }
}