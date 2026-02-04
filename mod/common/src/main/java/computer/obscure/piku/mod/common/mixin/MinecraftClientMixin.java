package computer.obscure.piku.mod.common.mixin;

import computer.obscure.piku.mod.common.ui.UIRenderer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Inject(method = "resizeDisplay", at = @At("TAIL"))
    private void onResolutionChanged(CallbackInfo ci) {
        UIRenderer.INSTANCE.onWindowResized();
    }
}