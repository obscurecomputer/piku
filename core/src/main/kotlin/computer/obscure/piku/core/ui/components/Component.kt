package computer.obscure.piku.core.ui.components

import computer.obscure.piku.core.classes.Vec2
import computer.obscure.piku.core.ui.classes.CompType
import computer.obscure.piku.core.ui.classes.RelativePosition
import computer.obscure.piku.core.ui.components.props.BaseProps
import java.util.*

sealed class Component {
    var name: String = ""
    val internalId: String = UUID.randomUUID().toString()
    var relativeTo: String? = null
    var relativePosition: RelativePosition? = null
    abstract val compType: CompType
    abstract val props: BaseProps

    abstract fun width(): Double
    abstract fun height(): Double

    var screenX: Int = 0
    var screenY: Int = 0
    var computedScale: Vec2 = Vec2(0.0, 0.0)
    var computedSize: Vec2? = null
    var computedPos: Vec2? = null
}