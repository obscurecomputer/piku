package me.znotchill.piku.common.ui
import me.znotchill.piku.common.ui.components.Component
import me.znotchill.piku.common.ui.components.Group

class UIWindow(val id: String) {
    var components = mutableMapOf<String, Component>()
    private val previousState = mutableMapOf<String, Component>()

    fun add(component: Component) {
        val key = componentKey(component)
        components[key] = component
    }

    fun remove(id: String) {
        components.remove(id)
    }

    private fun componentKey(comp: Component): String = comp.internalId

    fun getComponentById(id: String?): Component? {
        return components[id]
    }

    /**
     * Perform a recursive search for a component by its ID.
     */
    fun getComponentByIdDeep(id: String): Component? {
        fun search(list: List<Component>): Component? {
            for (c in list) {
                if (c.internalId == id) return c
                if (c is Group) {
                    search(c.props.components)?.let { return it }
                }
            }
            return null
        }

        return search(components.values.toList())
    }
}
