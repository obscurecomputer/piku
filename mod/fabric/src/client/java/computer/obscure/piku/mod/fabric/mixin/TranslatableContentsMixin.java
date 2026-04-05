package computer.obscure.piku.mod.fabric.mixin;

import computer.obscure.piku.mod.fabric.PikuClient;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(TranslatableContents.class)
public class TranslatableContentsMixin {

    @Shadow
    private List<FormattedText> decomposedParts;

    @Final
    @Shadow
    private String key;

    @Inject(method = "decompose", at = @At("TAIL"))
    private void applyMiniMessage(CallbackInfo ci) {
        List<FormattedText> newParts = new ArrayList<>();

        for (FormattedText part : this.decomposedParts) {
            String raw = extractString(part);

            // basic check for minimessage
            if (raw.contains("<")) {
                var adventure = PikuClient.Companion.getMiniMessage().deserialize(raw);

                var mcComponent = MinecraftClientAudiences
                        .of()
                        .asNative(adventure);

                newParts.add(mcComponent);
            } else {
                newParts.add(part);
            }
        }

        this.decomposedParts = newParts;
    }

    @Unique
    private static String extractString(FormattedText text) {
        StringBuilder sb = new StringBuilder();

        text.visit((style, str) -> {
            sb.append(str);
            return Optional.empty();
        }, Style.EMPTY);

        return sb.toString();
    }
}