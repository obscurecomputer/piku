package computer.obscure.piku.mod.fabric.mixin;

import computer.obscure.piku.mod.fabric.PikuClient;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {

    @Unique
    private String piku$cachedRaw;
    @Unique
    private Component piku$cachedComponent;

    @Redirect(
            method = "extractLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V"
            )
    )
    private void redirectBrandText(GuiGraphicsExtractor graphics, Font font, String str, int x, int y, int color, boolean dropShadow) {
        if (str != null && str.contains("<") && str.contains(">")) {
            if (!str.equals(piku$cachedRaw)) {
                var adventure = PikuClient.Companion.getMiniMessage().deserialize(str);
                piku$cachedComponent = MinecraftClientAudiences.of().asNative(adventure);
                piku$cachedRaw = str;
            }
            graphics.text(font, piku$cachedComponent, x, y, color, dropShadow);
        } else {
            graphics.text(font, str, x, y, color, dropShadow);
        }
    }

    @Redirect(
            method = "extractLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Font;width(Ljava/lang/String;)I"
            )
    )
    private int redirectBrandWidth(Font font, String str) {
        if (str != null && str.contains("<") && str.contains(">")) {
            if (!str.equals(piku$cachedRaw)) {
                var adventure = PikuClient.Companion.getMiniMessage().deserialize(str);
                piku$cachedComponent = MinecraftClientAudiences.of().asNative(adventure);
                piku$cachedRaw = str;
            }
            return font.width(piku$cachedComponent);
        }
        return font.width(str);
    }
}