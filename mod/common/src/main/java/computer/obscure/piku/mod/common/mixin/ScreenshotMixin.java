package computer.obscure.piku.mod.common.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import computer.obscure.piku.mod.common.Client;
import computer.obscure.piku.core.scripting.api.LuaTextInstance;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.io.File;
import java.util.function.Consumer;

@Mixin(Screenshot.class)
public class ScreenshotMixin {
    @ModifyVariable(
            method = "grab(Ljava/io/File;Ljava/lang/String;Lcom/mojang/blaze3d/pipeline/RenderTarget;ILjava/util/function/Consumer;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static Consumer<Component> piku$interceptScreenshot(Consumer<Component> messageReceiver, File gameDirectory, String fileName) {
        return (text) -> {
            if (Client.customScreenshotMessage != null && Client.customScreenshotInstance != null) {
                String defaultText = text.getString();
                String filename = defaultText.substring(defaultText.lastIndexOf(" ") + 1);
                File screenshotFile = new File(new File(gameDirectory, "screenshots"), filename);

                Component finalMessage = piku$convertAndInjectPath(Client.customScreenshotMessage, screenshotFile);
                messageReceiver.accept(finalMessage);
            } else {
                messageReceiver.accept(text);
            }
        };
    }

    @Unique
    private static Component piku$convertAndInjectPath(net.kyori.adventure.text.Component adventure, File file) {
        JsonElement json = GsonComponentSerializer.gson().serializeToTree(adventure);

        Component minecraft = ComponentSerialization.CODEC
                .parse(JsonOps.INSTANCE, json)
                .getOrThrow(error ->
                        new RuntimeException("Failed to parse Component: " + error));

        return piku$injectOpenFileRecursive(minecraft != null ? minecraft.copy() : Component.empty(), Client.customScreenshotInstance, file);
    }

    @Unique
    private static Component piku$injectOpenFileRecursive(Component component, LuaTextInstance luaInstance, File file) {
        MutableComponent result = component.copy();

        if (luaInstance != null && "open_file".equalsIgnoreCase(luaInstance.getClickEventType())) {
            result.withStyle(style -> style.withClickEvent(new ClickEvent.OpenFile(file.getAbsoluteFile())));
        }

        for (int i = 0; i < component.getSiblings().size(); i++) {
            Component sibling = component.getSiblings().get(i);
            LuaTextInstance childLua = (luaInstance != null && i < luaInstance.getChildren().size())
                    ? luaInstance.getChildren().get(i) : null;

            result.append(piku$injectOpenFileRecursive(sibling, childLua, file));
        }

        return result;
    }
}