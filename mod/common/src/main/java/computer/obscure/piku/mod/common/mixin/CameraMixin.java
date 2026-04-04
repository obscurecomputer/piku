package computer.obscure.piku.mod.common.mixin;

import computer.obscure.piku.mod.common.camera.CinematicCamera;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Camera.class, remap = false)
public abstract class CameraMixin {

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Shadow
    protected abstract void setPosition(Vec3 vec3);

    @Inject(
        method = "setup",
        at = @At("TAIL")
    )
    private void overrideCamera(
        Level level,
        Entity entity,
        boolean detached,
        boolean inverted,
        float partialTick,
        CallbackInfo ci
    ) {if (!CinematicCamera.INSTANCE.getActive()) return;

        Vec3 pos = new Vec3(
                Mth.lerp(partialTick, CinematicCamera.INSTANCE.getPrevPos().x, CinematicCamera.INSTANCE.getPos().x),
                Mth.lerp(partialTick, CinematicCamera.INSTANCE.getPrevPos().y, CinematicCamera.INSTANCE.getPos().y),
                Mth.lerp(partialTick, CinematicCamera.INSTANCE.getPrevPos().z, CinematicCamera.INSTANCE.getPos().z)
        );

        float yaw = (float) Mth.rotLerp(
                partialTick,
                CinematicCamera.INSTANCE.getPrevYaw(),
                CinematicCamera.INSTANCE.getYaw()
        );

        float pitch = (float) Mth.lerp(
                partialTick,
                CinematicCamera.INSTANCE.getPrevPitch(),
                CinematicCamera.INSTANCE.getPitch()
        );

        this.setPosition(pos);
        this.setRotation(yaw, pitch);
    }
}