package computer.obscure.piku.mod.common.mixin;

import computer.obscure.piku.mod.common.Client;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {

    @Inject(
        method = "renderHotbarAndDecorations",
        at = @At("HEAD"),
        cancellable = true
    )
    private void piku$hideHotbar(
            GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci
    ) {
        if (Client.hideHotbar) {
            ci.cancel();
        }
    }

    @Inject(
            method = "renderEffects",
            at = @At("HEAD"),
            cancellable = true
    )
    private void piku$hideStatusBars(
            GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci
    ) {
        if (Client.hideHotbar) {
            ci.cancel();
        }
    }

//    @Inject(
//            method = "render",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private void piku$skipMainHud(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
//        if (Client.hideHotbar) {
//            ci.cancel();
//        }
//    }
}