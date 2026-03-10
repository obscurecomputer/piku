package computer.obscure.piku.mod.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import computer.obscure.piku.mod.common.Client;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class HeldItemRendererMixin {
    @Shadow private float mainHandHeight;
    @Shadow private float oMainHandHeight;
    @Shadow private float offHandHeight;
    @Shadow private float oOffHandHeight;

    @Inject(
            method = "renderItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void piku$hideArm(
            LivingEntity livingEntity,
            ItemStack itemStack,
            ItemDisplayContext itemDisplayContext,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int i,
            CallbackInfo ci
    ) {
        if (Client.hideArm) {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    void piku$disableArmSwing(CallbackInfo ci) {
        if (Client.mouseButtonsLocked) {
            this.mainHandHeight = 1.0f;
            this.oMainHandHeight = 1.0f;
            this.offHandHeight = 1.0f;
            this.oOffHandHeight = 1.0f;
        }
    }
}