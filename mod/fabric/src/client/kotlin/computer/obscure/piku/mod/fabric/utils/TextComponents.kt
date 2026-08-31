package computer.obscure.piku.mod.fabric.utils

import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences
import net.kyori.adventure.text.Component
import net.minecraft.network.chat.FormattedText

fun Component.toNative(): FormattedText {
    return MinecraftClientAudiences.of().asNative(this)
}
fun FormattedText.toNativeComponent(): net.minecraft.network.chat.Component {
    return this as? net.minecraft.network.chat.Component
        ?: net.minecraft.network.chat.Component.literal(string)
}

fun Component.toNativeComponent(): net.minecraft.network.chat.Component {
    return this.toNative().toNativeComponent()
}

fun net.minecraft.network.chat.Component.toAdventure(): Component {
    return MinecraftClientAudiences.of().asAdventure(this)
}