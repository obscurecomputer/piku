package computer.obscure.piku.mod.fabric.mixin;

import computer.obscure.piku.mod.fabric.Client;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Hud.class, remap = false)
public class HudMixin {

    @Inject(
            method = "extractHotbarAndDecorations",
            at = @At("HEAD"),
            cancellable = true
    )
    private void piku$hideHotbar(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker,
            CallbackInfo ci
    ) {
        if (Client.hideHotbar) {
            ci.cancel();
        }
    }

    @Inject(
            method = "extractRenderState",
            at = @At("HEAD"),
            cancellable = true
    )
    private void piku$cinematicHud(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker,
            CallbackInfo ci
    ) {
        if (Client.hideHUD) {
            ci.cancel();
        }
    }
}