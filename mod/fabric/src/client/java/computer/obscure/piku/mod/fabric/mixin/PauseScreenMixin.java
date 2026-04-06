package computer.obscure.piku.mod.fabric.mixin;

import computer.obscure.piku.mod.fabric.Client;
import computer.obscure.piku.mod.fabric.MenuButton;
import computer.obscure.piku.mod.fabric.MenuConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PauseScreen.class)
public class PauseScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void removeButtons(CallbackInfo ci) {

        ScreenAccessor accessor = (ScreenAccessor) this;

        accessor.getRenderables().removeIf(this::shouldRemove);
        accessor.getChildren().removeIf(this::shouldRemove);
        accessor.getNarratables().removeIf(this::shouldRemove);

        repositionRemainingButtons(accessor);
    }

    @Unique
    private void repositionRemainingButtons(ScreenAccessor accessor) {
        List<Button> remaining = accessor.getRenderables().stream()
                .filter(r -> r instanceof Button)
                .map(r -> (Button) r)
                .toList();

        PauseScreen screen = (PauseScreen)(Object) this;
        MenuConfig config = Client.menuConfigs.getPause();

        for (Button button : remaining) {
            if (!(button.getMessage().getContents() instanceof TranslatableContents contents)) continue;
            String key = contents.getKey();
            MenuButton menuButton = config.getButtons().get(key);
            if (menuButton == null) continue;

            button.setX((int) menuButton.getPos().getX());
            button.setY((int) menuButton.getPos().getY());
            button.setWidth((int) menuButton.getSize().getX());
            button.setHeight((int) menuButton.getSize().getY());
        }
    }

    @Unique
    private boolean shouldRemove(Object widget) {
        if (widget instanceof Button button) {
            if (button.getMessage().getContents() instanceof TranslatableContents contents) {
                return Client.menuConfigs.getPause().getHidden().contains(contents.getKey());
            }
        }
        return false;
    }
}