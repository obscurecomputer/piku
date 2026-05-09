package computer.obscure.piku.core.ui.components

import computer.obscure.piku.core.ui.classes.CompType
import computer.obscure.piku.core.ui.components.props.ProgressBarProps

open class ProgressBar(
    override val props: ProgressBarProps
) : Component() {
    override val compType: CompType = CompType.PROGRESS_BAR

    override fun width(): Double {
        return props.size.x
    }

    override fun height(): Double {
        return props.size.y
    }
}