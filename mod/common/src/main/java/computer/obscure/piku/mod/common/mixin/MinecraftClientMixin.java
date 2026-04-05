package computer.obscure.piku.mod.common.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import computer.obscure.piku.core.classes.Vec2;
import computer.obscure.piku.core.scripting.api.LuaEventData;
import computer.obscure.piku.mod.common.Piku;
import computer.obscure.piku.mod.common.scripting.api.LuaClientEvents;
import computer.obscure.piku.mod.common.ui.UIRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.dialog.MultiButtonDialogScreen;
import net.minecraft.server.dialog.Dialog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftClientMixin {

    @Inject(method = "resizeDisplay", at = @At("TAIL"))
    private void onResolutionChanged(CallbackInfo ci) {
        UIRenderer.INSTANCE.onWindowResized();
    }


    @Inject(method = "setScreen", at = @At("HEAD"))
    private void onSetScreen(Screen screen, CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        Screen currentScreen = minecraft.screen;

        if (currentScreen != null && screen == null) {
            this.handleGenericScreenClose(currentScreen);

            if (currentScreen instanceof MultiButtonDialogScreen) {
                this.handleDialogClose(currentScreen);
            }
        }
    }

    private void handleGenericScreenClose(Screen screen) {
        java.util.Map<String, Object> data = new java.util.HashMap<>();

        data.put("screen_name", screen.getClass().getSimpleName());
        data.put("screen_class", screen.getClass().getName());
        data.put("title", screen.getTitle().getString());
        data.put("height", screen.height);
        data.put("width", screen.width);
        data.put("size", new Vec2(screen.width, screen.height));

        Piku.Companion.getEngine().getEvents().fire(
                "client.screen_close",
                new LuaEventData(data)
        );
    }

    private void handleDialogClose(Screen closedScreen) {
        if (closedScreen instanceof MultiButtonDialogScreen) {
            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("type", "multi_button");

            LuaEventData eventData = new LuaEventData(data);

            Piku.Companion.getEngine().getEvents().fire(
                    "client.dialog_close",
                    eventData
            );
        }
    }
}