package me.znotchill.piku.common.ui.components.props

import me.znotchill.piku.common.ui.classes.FlowDirection

open class FlowProps(
    var gap: Float = 0f,
    var direction: FlowDirection = FlowDirection.HORIZONTAL
) : CollectionProps()