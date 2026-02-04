package computer.obscure.piku.fabric.mixin;

import computer.obscure.piku.fabric.Client;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
//    @Shadow private float equipProgressMainHand;
//    @Shadow private float lastEquipProgressMainHand;
//    @Shadow private float equipProgressOffHand;
//    @Shadow private float lastEquipProgressOffHand;

    @Inject(
        method = "renderFirstPersonItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void piku$hideArm(
            AbstractClientPlayerEntity player,
            float tickProgress,
            float pitch,
            Hand hand,
            float swingProgress,
            ItemStack item,
            float equipProgress,
            MatrixStack matrices,
            OrderedRenderCommandQueue orderedRenderCommandQueue,
            int light,
            CallbackInfo ci
    ) {
        if (Client.hideArm) {
            ci.cancel();
        }
    }

    @Inject(method = "updateHeldItems", at = @At("TAIL"))
    void piku$disableArmSwing(CallbackInfo ci) {
        // TODO: fix this
        // Items do not render when you hold them!
//        if (Client.mouseButtonsLocked) {
//            // force reset the player's swing
//            equipProgressMainHand = 1f;
//            lastEquipProgressMainHand = 1f;
//            equipProgressOffHand = 1f;
//            lastEquipProgressOffHand = 1f;
//        }
    }
}