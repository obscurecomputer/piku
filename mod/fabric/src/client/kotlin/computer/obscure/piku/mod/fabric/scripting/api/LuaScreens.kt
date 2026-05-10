package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.mod.fabric.scripting.api.screen.LuaCustomScreen
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.*
import net.minecraft.client.gui.screens.achievement.StatsScreen
import net.minecraft.client.gui.screens.options.*

class LuaScreens : TwineNative("screens") {

    private val registry: MutableMap<String, () -> Screen> = mutableMapOf(
        "title" to { TitleScreen() },
        "options" to { OptionsScreen(Minecraft.getInstance().screen!!, Minecraft.getInstance().options) },
        "achievements" to { StatsScreen(Minecraft.getInstance().screen!!, Minecraft.getInstance().player!!.stats) },
    )

    init {
        if (FabricLoader.getInstance().isModLoaded("modmenu")) {
            registry["mods"] = {
                Class.forName("com.terraformersmc.modmenu.gui.ModsScreen")
                    .getConstructor(Screen::class.java)
                    .newInstance(Minecraft.getInstance().screen) as Screen
            }
        }
    }

    @TwineFunction
    fun create(title: String): LuaCustomScreen {
        return LuaCustomScreen(title)
    }

    @TwineFunction
    fun close() {
        Minecraft.getInstance().setScreen(null)
    }

    @TwineFunction
    fun open(id: String) {
        val factory = registry[id] ?: error("Unknown screen: $id")
        Minecraft.getInstance().execute {
            Minecraft.getInstance().setScreen(factory())
        }
    }
}
