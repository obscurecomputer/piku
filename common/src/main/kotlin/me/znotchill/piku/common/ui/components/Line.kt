package me.znotchill.piku.common.ui.components

import me.znotchill.piku.common.ui.classes.CompType
import me.znotchill.piku.common.ui.components.props.LineProps

open class Line(
    override val props: LineProps
) : Component() {
    override val compType: CompType = CompType.LINE

    override fun width(): Float {
        return 1f
    }

    override fun height(): Float {
        return 1f
    }
}