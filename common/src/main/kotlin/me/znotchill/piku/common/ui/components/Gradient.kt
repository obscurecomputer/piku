package me.znotchill.piku.common.ui.components

import me.znotchill.piku.common.ui.classes.CompType
import me.znotchill.piku.common.ui.components.props.GradientProps

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