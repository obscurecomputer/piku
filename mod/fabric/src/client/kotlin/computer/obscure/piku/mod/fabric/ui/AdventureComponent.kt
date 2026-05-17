package computer.obscure.piku.mod.fabric.ui

import computer.obscure.piku.core.scripting.api.LuaTextInstance
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences
import net.kyori.adventure.text.Component
import net.minecraft.network.chat.FormattedText

fun Component.toNative(): FormattedText {
    return MinecraftClientAudiences.of().asNative(this)
}

fun LuaTextInstance.toMcComponent(): net.minecraft.network.chat.Component {
    return MinecraftClientAudiences.of().asNative(toComponent())
}

fun FormattedText.toNativeComponent(): net.minecraft.network.chat.Component {
    return this as? net.minecraft.network.chat.Component
        ?: net.minecraft.network.chat.Component.literal(getString())
}