package computer.obscure.piku.fabric.ui.events

import computer.obscure.piku.common.ui.UIWindow
import computer.obscure.piku.common.ui.events.PropertyAnimation

class UIEventContext(
    val currentWindow: () -> UIWindow?,
    val enqueueAnimation: (PropertyAnimation<*, *>) -> Unit
)