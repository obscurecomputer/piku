package computer.obscure.piku.mod.fabric.mixin;

import computer.obscure.piku.core.scripting.api.LuaVec2Instance;
import computer.obscure.piku.mod.fabric.PikuClient;
import computer.obscure.piku.mod.fabric.scripting.api.screen.LuaScreenButtons;
import computer.obscure.piku.mod.fabric.ui.UIRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.dialog.MultiButtonDialogScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import computer.obscure.piku.mod.fabric.utils.ScreenKt;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = Minecraft.class)
public abstract class MinecraftClientMixin {

    @Shadow
    protected abstract boolean isMultiplayerServer();

    @Inject(method = "resizeDisplay", at = @At("TAIL"))
    private void onResolutionChanged(CallbackInfo ci) {
        if (this.isMultiplayerServer())
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
        } else if (screen != null) {
            this.handleGenericScreenOpen(screen);
        }
    }

    @Unique
    private void handleGenericScreenClose(Screen screen) {
        Map<String, Object> data = new HashMap<>();

        data.put("name", ScreenKt.getRemappedName(screen));
        data.put("class", screen.getClass().getName());
        data.put("title", screen.getTitle().getString());
        data.put("height", screen.height);
        data.put("width", screen.width);
        data.put("size", new LuaVec2Instance(screen.width, screen.height));
        data.put("buttons", new LuaScreenButtons(screen));

        PikuClient.Companion.getEngine().getEvents().fire(
                "client.screen_close",
                data
        );
    }

    @Unique
    private void handleGenericScreenOpen(Screen screen) {
        Map<String, Object> data = new HashMap<>();

        data.put("name", ScreenKt.getRemappedName(screen));
        data.put("class", screen.getClass().getName());
        data.put("title", screen.getTitle().getString());
        data.put("height", screen.height);
        data.put("width", screen.width);
        data.put("size", new LuaVec2Instance(screen.width, screen.height));
        data.put("buttons", new LuaScreenButtons(screen));

        PikuClient.Companion.getEngine().getEvents().fire(
                "client.screen_open",
                data
        );
    }

    @Unique
    private void handleDialogClose(Screen closedScreen) {
        if (closedScreen instanceof MultiButtonDialogScreen) {
            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("type", "multi_button");

            PikuClient.Companion.getEngine().getEvents().fire(
                    "client.dialog_close",
                    data
            );
        }
    }
}