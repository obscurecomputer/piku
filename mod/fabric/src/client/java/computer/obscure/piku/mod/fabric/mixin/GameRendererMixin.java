package computer.obscure.piku.mod.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import computer.obscure.piku.mod.fabric.Client;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class, remap = false)
public class GameRendererMixin {
    @ModifyVariable(
            method = "bobView",
            at = @At("STORE"),
            ordinal = 1
    )
    private float applyCustomBobStrength(float h) {
        return h * Client.bobbingStrength;
    }

    @Inject(method = "bobHurt", at = @At("HEAD"))
    private void tiltViewWhenHurt(PoseStack matrices, float f, CallbackInfo ci) {
        if (Client.rotation.getX() != 0f || Client.rotation.getY() != 0f || Client.rotation.getZ() != 0f) {
            float pitchRad = (float) Math.toRadians(Client.rotation.getX());
            float yawRad   = (float) Math.toRadians(Client.rotation.getY());
            float rollRad  = (float) Math.toRadians(Client.rotation.getZ());

            matrices.mulPose(new Quaternionf().rotateXYZ(yawRad, pitchRad, rollRad));
        }

        // Y and Z are swapped intentionally
        matrices.translate(
                Client.cameraOffsetX * -1,
                Client.cameraOffsetZ * -1,
                Client.cameraOffsetY * -1
        );
    }
}
