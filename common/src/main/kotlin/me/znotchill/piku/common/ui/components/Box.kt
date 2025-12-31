package me.znotchill.piku.common.ui.components

import me.znotchill.piku.common.ui.classes.CompType
import me.znotchill.piku.common.ui.components.props.BoxProps

open class Box(
    override val props: BoxProps
) : Component() {
    override val compType: CompType = CompType.BOX

    override fun width(): Float {
        return props.size.x
    }

    override fun height(): Float {
        return props.size.y
    }
}