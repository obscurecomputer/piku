package me.znotchill.piku.client

import me.znotchill.piku.common.scripting.api.LuaEventData
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import org.lwjgl.glfw.GLFW

object InputTracker {
    private val keyStates = mutableMapOf<Int, Boolean>()
    private val mouseStates = mutableMapOf<Int, Boolean>()

    fun init() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            pollKeyboard(client)
            pollMouse(client)
        }
    }

    fun getKeyName(key: Int): String = when (key) {
        GLFW.GLFW_KEY_LEFT -> "left_arrow"
        GLFW.GLFW_KEY_RIGHT -> "right_arrow"
        GLFW.GLFW_KEY_UP -> "up_arrow"
        GLFW.GLFW_KEY_DOWN -> "down_arrow"
        GLFW.GLFW_KEY_TAB -> "tab"
        GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT -> "shift"
        GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_RIGHT_CONTROL -> "ctrl"
        GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_RIGHT_ALT -> "alt"
        GLFW.GLFW_KEY_CAPS_LOCK -> "caps_lock"
        in GLFW.GLFW_KEY_F1..GLFW.GLFW_KEY_F25 -> "f${key - GLFW.GLFW_KEY_F1 + 1}"
        else -> GLFW.glfwGetKeyName(key, 0)?.lowercase() ?: "unknown"
    }

    private fun pollKeyboard(client: MinecraftClient) {
        val window = client.window.handle

        for (key in 32..GLFW.GLFW_KEY_LAST) {
            val pressed = GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS
            val prev = keyStates[key] ?: false

            if (pressed != prev) {
                keyStates[key] = pressed
                val event = LuaEventData(
                    mapOf<String, Any?>(
                        "key" to getKeyName(key),
                        "action" to if (pressed) "press" else "release"
                    )
                )
                PikuClient.engine.events.fire("client.key_update", event.table)
            }
        }
    }

    private fun pollMouse(client: MinecraftClient) {
        val window = client.window.handle
        for (button in 0..7) { // track first 8 mouse buttons
            val pressed = GLFW.glfwGetMouseButton(window, button) == GLFW.GLFW_PRESS
            val prev = mouseStates[button] ?: false

            if (pressed != prev) {
                mouseStates[button] = pressed
                val event = LuaEventData(
                    mapOf(
                        "button" to button,
                        "action" to if (pressed) "press" else "release",
                        "x" to client.mouse.x,
                        "y" to client.mouse.y
                    )
                )
                PikuClient.engine.events.fire("client.mouse_update", event.table)
            }
        }
    }
}
