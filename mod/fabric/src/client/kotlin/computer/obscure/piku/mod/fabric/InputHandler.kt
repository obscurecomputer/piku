package computer.obscure.piku.mod.fabric

import computer.obscure.piku.core.service.PikuService
import computer.obscure.piku.mod.fabric.compat.ModCompat
import computer.obscure.piku.mod.fabric.controlify.ControlifyIntegration
import computer.obscure.piku.mod.fabric.scripting.api.LuaKeyBind
import computer.obscure.piku.mod.fabric.ui.ControlifyUI
import computer.obscure.piku.mod.fabric.ui.classes.UIEvent
import computer.obscure.piku.mod.fabric.ui.menu.UIMenu
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW

object InputHandler : PikuService {

    val mc = Minecraft.getInstance()

    private val keyStates = mutableMapOf<Int, Boolean>()
    private val mouseStates = mutableMapOf<Int, Boolean>()
    private val luaInputQueue = mutableListOf<LuaKeyBind>()

    var keyboardCaptured = true
    var mouseCaptured = true
    var scrollCaptured = true

    fun init() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            luaInputQueue.toList().forEach {
                it.setDown(false)
                luaInputQueue.remove(it)
            }

            if (ModCompat.controlifyLoaded) {
                ControlifyIntegration.tick()
                ControlifyUI.tick()
            }
        }
    }

    fun registerCallbacks(windowHandle: Long) {
        // save minecraft's existing callbacks because glfw only allows
        // one callback per event :/
        val prevKeyCallback = GLFW.glfwSetKeyCallback(windowHandle, null)
        val prevMouseCallback = GLFW.glfwSetMouseButtonCallback(windowHandle, null)
        val prevScrollCallback = GLFW.glfwSetScrollCallback(windowHandle, null)

        GLFW.glfwSetKeyCallback(windowHandle) { window, key, scancode, action, mods ->
            prevKeyCallback?.invoke(window, key, scancode, action, mods)
            if (!shouldHandleInput(InputMethod.KEYBOARD)) return@glfwSetKeyCallback
            val pressed = action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT
            val prev = keyStates[key] ?: false
            if (pressed != prev || action == GLFW.GLFW_REPEAT) {
                keyStates[key] = pressed
                val data = mapOf<String, Any?>(
                    "key" to getKeyName(key),
                    "action" to when (action) {
                        GLFW.GLFW_PRESS -> "press"
                        GLFW.GLFW_RELEASE -> "release"
                        GLFW.GLFW_REPEAT -> "repeat"
                        else -> "unknown"
                    }
                )
                PikuClient.engine!!.events.fire("client.key_update", data)
            }
        }

        GLFW.glfwSetMouseButtonCallback(windowHandle) { window, button, action, mods ->
            prevMouseCallback?.invoke(window, button, action, mods)
            if (!shouldHandleInput(InputMethod.MOUSE)) return@glfwSetMouseButtonCallback
            val pressed = action == GLFW.GLFW_PRESS
            val prev = mouseStates[button] ?: false
            if (pressed != prev) {
                mouseStates[button] = pressed
                fireClick(
                    MouseButton.fromIndex(button),
                    pressed,
                    mc.mouseHandler.xpos(), mc.mouseHandler.ypos()
                )
            }
        }

        GLFW.glfwSetScrollCallback(windowHandle) { window, deltaX, deltaY ->
            prevScrollCallback?.invoke(window, deltaX, deltaY)
            if (!shouldHandleInput(InputMethod.MOUSE_SCROLL)) return@glfwSetScrollCallback
            val data = mapOf(
                "deltaX" to deltaX,
                "deltaY" to deltaY,
                "x" to mc.mouseHandler.xpos(),
                "y" to mc.mouseHandler.ypos()
            )
            PikuClient.engine!!.events.fire("client.scroll", data)
        }
    }

    private fun shouldHandleInput(method: InputMethod): Boolean {
        if (method == InputMethod.KEYBOARD && !keyboardCaptured) return false
        if (method == InputMethod.MOUSE && !mouseCaptured) return false
        if (method == InputMethod.MOUSE_SCROLL && !scrollCaptured) return false

        if (mc.gui.screen() != null && method == InputMethod.KEYBOARD)
            return mc.gui.screen() is UIMenu
        return Client.connectedToServer && mc.player != null
    }

    fun queueInputUp(luaKeyBind: LuaKeyBind) {
        luaInputQueue.add(luaKeyBind)
    }
    override fun shutdown() {
        luaInputQueue.clear()
    }

    fun getKeyName(key: Int): String = when (key) {
        GLFW.GLFW_KEY_LEFT -> "left_arrow"
        GLFW.GLFW_KEY_RIGHT -> "right_arrow"
        GLFW.GLFW_KEY_UP -> "up_arrow"
        GLFW.GLFW_KEY_DOWN -> "down_arrow"

        GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> "enter"
        GLFW.GLFW_KEY_ESCAPE -> "escape"
        GLFW.GLFW_KEY_SPACE -> "space"
        GLFW.GLFW_KEY_BACKSPACE -> "backspace"
        GLFW.GLFW_KEY_DELETE -> "delete"
        GLFW.GLFW_KEY_INSERT -> "insert"

        GLFW.GLFW_KEY_PAGE_UP -> "page_up"
        GLFW.GLFW_KEY_PAGE_DOWN -> "page_down"
        GLFW.GLFW_KEY_HOME -> "home"
        GLFW.GLFW_KEY_END -> "end"

        GLFW.GLFW_KEY_TAB -> "tab"
        GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT -> "shift"
        GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_RIGHT_CONTROL -> "ctrl"
        GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_RIGHT_ALT -> "alt"
        GLFW.GLFW_KEY_CAPS_LOCK -> "caps_lock"
        GLFW.GLFW_KEY_LEFT_SUPER, GLFW.GLFW_KEY_RIGHT_SUPER -> "os_key"

        in GLFW.GLFW_KEY_F1..GLFW.GLFW_KEY_F25 -> "f${key - GLFW.GLFW_KEY_F1 + 1}"

        else -> {
            val name = GLFW.glfwGetKeyName(key, 0)
            name?.lowercase() ?: "key_$key"
        }
    }

    fun getMouseButtonName(button: Int): String {
        return "mouse_$button"
    }

    fun fireClick(
        button: MouseButton,
        pressed: Boolean,
        x: Double, y: Double,
        inMenu: Boolean = false,
    ) {
        val data = mapOf(
            "button" to button.index,
            "action" to if (pressed) "press" else "release",
            "x" to x,
            "y" to y,
            "inMenu" to inMenu
        )
        PikuClient.engine!!.events.fire("client.mouse_update", data)
    }

    fun fireMenuClick(
        event: UIEvent
    ) {
        PikuClient.engine!!.events.fire("client.menu.mouse_update", mapOf("event" to event))
    }
}

enum class MouseButton(val index: Int) {
    LEFT(0),
    RIGHT(1),
    MIDDLE(2);

    companion object {
        fun fromIndex(index: Int): MouseButton {
            return entries.firstOrNull { it.index == index } ?: LEFT
        }
    }
}

enum class InputMethod {
    KEYBOARD,
    MOUSE,
    MOUSE_SCROLL,
    CONTROLLER
}