package computer.obscure.piku.client.mixin;

import computer.obscure.piku.client.camera.CinematicCamera;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    protected abstract void setPos(Vec3d pos);

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(
        method = "update",
        at = @At("TAIL")
    )
    private void overrideCamera(
        BlockView area,
        Entity focusedEntity,
        boolean thirdPerson,
        boolean inverseView,
        float tickDelta,
        CallbackInfo ci
    ) {
        if (!CinematicCamera.INSTANCE.getActive()) return;

        float t = tickDelta;

        Vec3d pos = CinematicCamera.INSTANCE.getPrevPos()
                .lerp(CinematicCamera.INSTANCE.getPos(), t);

        float yaw = MathHelper.lerpAngleDegrees(
                t,
                CinematicCamera.INSTANCE.getPrevYaw(),
                CinematicCamera.INSTANCE.getYaw()
        );

        float pitch = MathHelper.lerp(
                t,
                CinematicCamera.INSTANCE.getPrevPitch(),
                CinematicCamera.INSTANCE.getPitch()
        );

        this.setPos(pos);
        this.setRotation(yaw, pitch);
    }
}