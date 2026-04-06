package computer.obscure.piku.mod.fabric

import computer.obscure.piku.core.classes.Vec2

data class MenuConfigs(
    val pause: MenuConfig = MenuConfig()
)

data class MenuConfig(
    val hidden: MutableList<String> = mutableListOf(),
    val buttons: MutableMap<String, MenuButton> = mutableMapOf(),
)

data class MenuButton(
    var pos: Vec2 = Vec2(0.0, 0.0),
    var size: Vec2 = Vec2(0.0, 0.0),
)