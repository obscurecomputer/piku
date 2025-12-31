package me.znotchill.piku.common.ui

import me.znotchill.piku.common.ui.components.Component
import me.znotchill.piku.common.ui.components.FlowContainer
import me.znotchill.piku.common.ui.components.Group
import me.znotchill.piku.common.ui.components.props.CollectionProps
import me.znotchill.piku.common.ui.components.props.FlowProps
import me.znotchill.piku.common.ui.dsl.CollectionBuilder

open class PikuUI(val id: String) {
    private val components = mutableListOf<Component>()

    fun build(): UIWindow {
        val window = UIWindow(id)
        components.forEach {
            it.window = window
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