package computer.obscure.piku.fabric.mixin;

import computer.obscure.piku.fabric.Client;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(
        method = "renderHotbar",
        at = @At("HEAD"),
        cancellable = true
    )
    private void piku$hideHotbar(
            DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci
    ) {
        if (Client.hideHotbar) {
            ci.cancel();
        }
    }

    @Inject(
            method = "renderStatusBars",
            at = @At("HEAD"),
            cancellable = true
    )
    private void piku$hideStatusBars(
            DrawContext context, CallbackInfo ci
    ) {
        if (Client.hideHotbar) {
            ci.cancel();
        }
    }

    @Inject(
            method = "renderMainHud",
            at = @At("HEAD"),
            cancellable = true
    )
    private void piku$skipMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (Client.hideHotbar) {
            ci.cancel();
        }
    }
}