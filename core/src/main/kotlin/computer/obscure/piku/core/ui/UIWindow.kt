package computer.obscure.piku.core.ui
import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.components.Group

class UIWindow(val id: String) {
    var components = mutableMapOf<String, Component>()

    private val idCache = mutableMapOf<String, Component>()

    fun getComponentById(id: String): Component? = idCache[id]

    fun registerRecursive(component: Component) {
        idCache[component.internalId] = component
        if (component is Group) {
            component.props.components.forEach { registerRecursive(it) }
        }
    }

    fun unregisterRecursive(id: String) {
        val comp = idCache.remove(id)
        if (comp is Group) {
            comp.props.components.forEach { unregisterRecursive(it.internalId) }
        }
    }

    fun add(component: Component) {
        components[component.internalId] = component
        registerRecursive(component)
    }

    fun remove(id: String) {
        unregisterRecursive(id)
        components.remove(id)
    }

    private fun componentKey(comp: Component): String = comp.internalId

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
