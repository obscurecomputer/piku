package computer.obscure.piku.core.states

import computer.obscure.piku.core.scripting.server.SharedStateManager
import java.util.UUID
import kotlin.reflect.KProperty

data class SharedState(
    val name: String,
    var value: Any? = null,
    var clientModifiable: Boolean = false,

    /**
     * The owner(s) of this [SharedState].
     */
    var owners: Any = listOf<Any>(),

    var onSet: (oldValue: Any?, newValue: Any?) -> Unit = { _, _ -> },

    /**
     * A random UUID given to each [SharedState] to avoid naming conflicts.
     */
    val internalId: UUID = UUID.randomUUID()
) {

    fun destroy() {
        println("Destroyed")
    }

    fun set(
        newValue: Any,
        exemptSyncOwners: List<Any> = listOf()
    ): SharedState {
        val oldValue = this.value
        this.value = newValue
        onSet(oldValue, newValue)

        SharedStateManager.piku.syncStateToOwners(
            owners,
            this,
            exemptSyncOwners
        )

        return this
    }

    fun get(): Any? = this.value

    // SharedState *can* be used as a delegate, but you lose the reference to the object
    // so it is generally not recommended
    // unfortunate, since it makes the usage much cleaner
    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): SharedState {
        SharedStateManager.addState(this)
        return this
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Any? {
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: Any?) {
        val oldValue = value
        value = newValue
        onSet(oldValue, newValue)
    }
}