package computer.obscure.piku.mod.common.mixin;

import computer.obscure.piku.mod.common.Client;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyVariable(
            method = "bobView",
            at = @At("STORE"),
            ordinal = 1
    )
    // TODO: figure out why this does not work
    private float applyCustomBobStrength(float h) {
        return h * Client.bobbingStrength;
    }
}
