package computer.obscure.piku.client.mixin;

import computer.obscure.piku.client.Client;
import computer.obscure.piku.common.scripting.api.LuaTextInstance;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.io.File;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotMixin {
    @ModifyVariable(
            method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;Lnet/minecraft/client/gl/Framebuffer;ILjava/util/function/Consumer;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static Consumer<Text> removeScreenshotMessage(Consumer<Text> messageReceiver, File gameDirectory, String fileName) {
        return (text) -> {
            if (Client.customScreenshotMessage != null && Client.customScreenshotInstance != null) {
                String defaultText = text.getString();
                String filename = defaultText.substring(defaultText.lastIndexOf(" ") + 1);
                File screenshotFile = new File(new File(gameDirectory, "screenshots"), filename);

                Text finalMessage = convertAndInjectPath(Client.customScreenshotMessage, screenshotFile);
                messageReceiver.accept(finalMessage);
            } else {
                messageReceiver.accept(text);
            }
        };
    }

    @Unique
    private static Text convertAndInjectPath(net.kyori.adventure.text.Component adventure, File file) {
        Text minecraft = MinecraftClientAudiences.of().asNative(adventure);
        return injectOpenFileRecursive(minecraft, Client.customScreenshotInstance, file);
    }

    @Unique
    private static Text injectOpenFileRecursive(Text component, LuaTextInstance luaInstance, File file) {
        MutableText result = component.copy();

        // Only apply the ClickEvent if this specific LuaTextInstance has a click event applied
        if (luaInstance != null && "open_file".equalsIgnoreCase(luaInstance.getClickEventType())) {
            result.setStyle(result.getStyle().withClickEvent(new ClickEvent.OpenFile(file)));
        }

        java.util.List<Text> siblings = result.getSiblings();
        if (!siblings.isEmpty()) {
            MutableText newResult = result.copyContentOnly().setStyle(result.getStyle());

            for (int i = 0; i < siblings.size(); i++) {
                Text sibling = siblings.get(i);

                LuaTextInstance childLua = null;
                if (luaInstance != null && i < luaInstance.getChildren().size()) {
                    childLua = luaInstance.getChildren().get(i);
                }

                newResult.append(injectOpenFileRecursive(sibling, childLua, file));
            }
            return newResult;
        }

        return result;
    }
}