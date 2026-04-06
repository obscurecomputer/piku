package computer.obscure.piku.mod.fabric.utils

import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.client.gui.screens.PauseScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.InventoryScreen

fun Screen.getRemappedName(): String {
    return when (this) {
        is PauseScreen -> "PauseScreen"
        is ChatScreen -> "ChatScreen"
        is InventoryScreen -> "InventoryScreen"
        else -> javaClass.simpleName
    }
}