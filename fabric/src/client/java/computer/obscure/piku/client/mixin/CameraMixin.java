package computer.obscure.piku.client.mixin;

import computer.obscure.piku.client.camera.CinematicCamera;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
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

        this.setPos(CinematicCamera.INSTANCE.getPos());
        this.setRotation(
            CinematicCamera.INSTANCE.getYaw(),
            CinematicCamera.INSTANCE.getPitch()
        );
    }
}