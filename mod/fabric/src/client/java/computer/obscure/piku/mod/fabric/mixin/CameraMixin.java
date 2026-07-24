package computer.obscure.piku.mod.fabric.mixin;

import computer.obscure.piku.mod.fabric.Client;
import computer.obscure.piku.mod.fabric.camera.CinematicCamera;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Camera.class, remap = false)
public abstract class CameraMixin {

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Shadow
    protected abstract void setPosition(Vec3 vec3);

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    void onGetFov(CallbackInfoReturnable<Float> cir) {
        Client.vanillaFov = cir.getReturnValue();

        boolean controlled = Client.fovControlled;
        float animated = Client.animatedFov;

        if (controlled) {
            cir.setReturnValue(animated);
        }
    }


    @Inject(
            method = "update",
            at = @At("TAIL")
    )
    private void afterUpdate(
            DeltaTracker deltaTracker,
            CallbackInfo ci
    ) {
        if (!CinematicCamera.INSTANCE.getActive()) {
            applyCameraTransform(deltaTracker, ci);
            return;
        }

        float tickDelta =
                deltaTracker.getGameTimeDeltaPartialTick(true);


        Vec3 pos = new Vec3(
                Mth.lerp(
                        tickDelta,
                        CinematicCamera.INSTANCE.getPrevPos().x,
                        CinematicCamera.INSTANCE.getPos().x
                ),
                Mth.lerp(
                        tickDelta,
                        CinematicCamera.INSTANCE.getPrevPos().y,
                        CinematicCamera.INSTANCE.getPos().y
                ),
                Mth.lerp(
                        tickDelta,
                        CinematicCamera.INSTANCE.getPrevPos().z,
                        CinematicCamera.INSTANCE.getPos().z
                )
        );


        float yaw = (float) Mth.rotLerp(
                tickDelta,
                CinematicCamera.INSTANCE.getPrevYaw(),
                CinematicCamera.INSTANCE.getYaw()
        );


        float pitch = (float) Mth.lerp(
                tickDelta,
                CinematicCamera.INSTANCE.getPrevPitch(),
                CinematicCamera.INSTANCE.getPitch()
        );


        setPosition(pos);
        setRotation(yaw, pitch);
    }

    private void applyCameraTransform(
            DeltaTracker deltaTracker,
            CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.getCameraEntity() == null)
            return;


        if (Client.rotation.x != 0 ||
                Client.rotation.y != 0) {

            Camera camera = (Camera)(Object)this;

            setRotation(
                    (float) (camera.yRot() + Client.rotation.y),
                    (float) (camera.xRot() + Client.rotation.x)
            );
        }

        Vec3 pos = ((Camera)(Object)this).position();

        setPosition(
                pos.add(
                        -Client.cameraOffsetX,
                        -Client.cameraOffsetZ,
                        -Client.cameraOffsetY
                )
        );
    }
}