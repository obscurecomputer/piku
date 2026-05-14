package computer.obscure.piku.mod.fabric.ui

import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences
import net.kyori.adventure.text.Component
import net.minecraft.network.chat.FormattedText

fun Component.toNative(): FormattedText {
    return MinecraftClientAudiences.of().asNative(this)
}