package computer.obscure.piku.mod.common

import computer.obscure.piku.core.scripting.api.LuaEventData
import computer.obscure.piku.mod.common.scripting.api.LuaKeyBind
import dev.architectury.event.events.client.ClientTickEvent
import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW

object InputHandler {
    private val keyStates = mutableMapOf<Int, Boolean>()
    private val mouseStates = mutableMapOf<Int, Boolean>()

    private val luaInputQueue = mutableListOf<LuaKeyBind>()

    fun init() {
        ClientTickEvent.CLIENT_POST.register { client ->
            luaInputQueue.toList().forEach {
                it.setDown(false)
                luaInputQueue.remove(it)
            }
             if (!Client.connectedToServer) return@register

            // player must be in-game (no menus open at all)
            if (client.screen != null) return@register

            // client's player entity must exist
            if (client.player == null) return@register

            pollMouse(client)
            pollKeyboard(client)
        }
    }

    /**
     * Queue a [LuaKeyBind] to be deactivated the tick after.
     */
    fun queueInputUp(luaKeyBind: LuaKeyBind) {
        luaInputQueue.add(luaKeyBind)
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

    private fun pollKeyboard(client: Minecraft) {
        val window = client.window.handle()

        for (key in 32..GLFW.GLFW_KEY_LAST) {
            val pressed = GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS
            val prev = keyStates[key] ?: false

            if (pressed != prev) {
                keyStates[key] = pressed
                val data = mapOf<String, Any?>(
                    "key" to getKeyName(key),
                    "action" to if (pressed) "press" else "release"
                )

                Piku.engine.events.fire("client.key_update", LuaEventData(data).table)
            }
        }
    }

    private fun pollMouse(client: Minecraft) {
        val window = client.window.handle()
        for (button in 0..7) {
            val pressed = GLFW.glfwGetMouseButton(window, button) == GLFW.GLFW_PRESS
            val prev = mouseStates[button] ?: false

            if (pressed != prev) {
                mouseStates[button] = pressed
                val data = mapOf(
                    "button" to button,
                    "action" to if (pressed) "press" else "release",
                    "x" to client.mouseHandler.xpos(),
                    "y" to client.mouseHandler.ypos()
                )

                Piku.engine.events.fire("client.mouse_update", LuaEventData(data).table)
            }
        }
    }
}