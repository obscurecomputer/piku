package computer.obscure.piku.common.ui.classes

import java.util.EnumSet

open class DirtyProps {
    private val dirtyFlags = EnumSet.noneOf(DirtyFlag::class.java)

    fun mark(flag: DirtyFlag) {
        dirtyFlags.add(flag)
    }

    fun isDirty(flag: DirtyFlag): Boolean = dirtyFlags.contains(flag)

    fun clear(flag: DirtyFlag) {
        dirtyFlags.remove(flag)
    }

    fun clearAll() {
        dirtyFlags.clear()
    }
}
