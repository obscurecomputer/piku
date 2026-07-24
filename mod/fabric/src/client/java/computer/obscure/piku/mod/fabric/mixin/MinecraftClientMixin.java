package computer.obscure.piku.mod.fabric.mixin;

//import computer.obscure.piku.mod.fabric.ui.UIRenderer;
import net.minecraft.client.Minecraft;
        import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
        import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class)
public abstract class MinecraftClientMixin {

    @Shadow
    protected abstract boolean isMultiplayerServer();

    @Inject(method = "resizeGui", at = @At("TAIL"))
    private void onResolutionChanged(CallbackInfo ci) {
//        if (this.isMultiplayerServer())
//            UIRenderer.INSTANCE.onWindowResized();
    }
}