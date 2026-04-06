package computer.obscure.piku.mod.fabric.scripting.api.screen

import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class CustomScreen(title: Component) : Screen(title) {
    private val initCallbacks = mutableListOf<() -> Unit>()

    fun onInit(block: () -> Unit) {
        initCallbacks.add(block)
    }

    fun addWidget(widget: AbstractWidget) {
        addRenderableWidget(widget)
    }

    override fun init() {
        initCallbacks.forEach { it() }
    }
}