package computer.obscure.piku.mod.common.utils

import net.minecraft.network.chat.Component
import kotlin.collections.iterator

fun parseMini(input: String): Component {
    var text = input

    val map = mapOf(
        "<black>" to "§0",
        "<dark_blue>" to "§1",
        "<dark_green>" to "§2",
        "<dark_aqua>" to "§3",
        "<dark_red>" to "§4",
        "<dark_purple>" to "§5",
        "<gold>" to "§6",
        "<gray>" to "§7",
        "<grey>" to "§7",
        "<dark_gray>" to "§8",
        "<dark_grey>" to "§8",
        "<blue>" to "§9",
        "<green>" to "§a",
        "<aqua>" to "§b",
        "<red>" to "§c",
        "<light_purple>" to "§d",
        "<yellow>" to "§e",
        "<white>" to "§f",

        "<obfuscated>" to "§k",
        "<bold>" to "§l",
        "<strikethrough>" to "§m",
        "<underline>" to "§n",
        "<underlined>" to "§n",
        "<italic>" to "§o",

        "<b>" to "§l",
        "<i>" to "§o",
        "<u>" to "§n",
        "<s>" to "§m",
        "<k>" to "§k",

        "<reset>" to "§r"
    )

    for ((k, v) in map) {
        text = text.replace(k, v, ignoreCase = true)
    }

    return Component.literal(text)
}
