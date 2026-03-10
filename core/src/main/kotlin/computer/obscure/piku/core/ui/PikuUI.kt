package computer.obscure.piku.core.ui

import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.components.FlowContainer
import computer.obscure.piku.core.ui.components.Group
import computer.obscure.piku.core.ui.components.props.CollectionProps
import computer.obscure.piku.core.ui.components.props.FlowProps
import computer.obscure.piku.core.ui.dsl.CollectionBuilder

open class PikuUI(val id: String) {
    private val components = mutableListOf<Component>()

    fun build(): UIWindow {
        val window = UIWindow(id)
        components.forEach {
            window.add(it)
        }
        return window
    }

    fun group(
        id: String = "",
        init: CollectionBuilder<CollectionProps>.() -> Unit
    ) {
        val builder = CollectionBuilder(build(), CollectionProps())
        builder.init()
        val group = builder.build()

        builder.props.components = group.toMutableList()
        components.add(
            Group(
                props = builder.props
            )
        )
    }

    fun flow(
        id: String = "",
        init: CollectionBuilder<FlowProps>.() -> Unit
    ) {
        val builder = CollectionBuilder(build(), FlowProps())
        builder.init()
        val group = builder.build()

        builder.props.components = group.toMutableList()
        components.add(
            FlowContainer(
                props = builder.props
            )
        )
    }

//    fun widget(widget: UIWidget, x: Int = 0, y: Int = 0, anchor: Anchor): UIComponentManager {
//        widget.x = x
//        widget.y = y
//        val builtWidget = widget.build()
//
//        builtWidget.forEach { component ->
//            components.add(component)
//        }
//
//        return UIComponentManager(widget)
//    }

    /**
     * Add a vararg [Component] to the window.
     */
    fun add(vararg componentList: Component) {
        componentList.forEach {
            components.add(it)
        }
    }
}