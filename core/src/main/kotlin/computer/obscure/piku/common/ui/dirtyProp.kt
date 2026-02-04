package computer.obscure.piku.common.ui

import computer.obscure.piku.common.ui.classes.DirtyFlag
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> dirtyProp(initial: T, flag: DirtyFlag, mark: (DirtyFlag) -> Unit) =
    object : ReadWriteProperty<Any?, T> {
        private var fieldValue = initial

        override fun getValue(thisRef: Any?, property: KProperty<*>): T = fieldValue

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            if (fieldValue != value) {
                fieldValue = value
                mark(flag)
            }
        }
    }