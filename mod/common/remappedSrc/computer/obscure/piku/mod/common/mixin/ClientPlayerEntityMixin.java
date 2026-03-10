package computer.obscure.piku.mod.common.mixin;

import computer.obscure.piku.mod.common.Client;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin {
    @Inject(method = "swing", at = @At("HEAD"), cancellable = true)
    void cancelSwing(InteractionHand hand, CallbackInfo ci) {
        if (Client.mouseButtonsLocked) {
            ci.cancel();
        }
    }
}