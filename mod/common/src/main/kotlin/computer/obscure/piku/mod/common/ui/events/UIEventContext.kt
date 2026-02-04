package computer.obscure.piku.mod.common.ui.events

import computer.obscure.piku.core.ui.UIWindow
import computer.obscure.piku.core.ui.events.PropertyAnimation

class UIEventContext(
    val currentWindow: () -> UIWindow?,
    val enqueueAnimation: (PropertyAnimation<*, *>) -> Unit
)