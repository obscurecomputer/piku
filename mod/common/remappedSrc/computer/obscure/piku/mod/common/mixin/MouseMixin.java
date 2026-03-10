package computer.obscure.piku.mod.common.mixin;

import computer.obscure.piku.mod.common.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {
    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    private void piku$lockCamera(double d, CallbackInfo ci) {
        if (Client.cameraLocked) {
            ci.cancel();
        }
    }

    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void piku$lockButtons(long windowHandle, MouseButtonInfo info, int action, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();

        if (Client.mouseButtonsLocked && !Client.emitMouseEvents) {
            if (mc.screen == null) {
                ci.cancel();
            }
        }
    }
}