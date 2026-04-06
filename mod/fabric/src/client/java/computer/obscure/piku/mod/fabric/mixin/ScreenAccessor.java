package computer.obscure.piku.mod.fabric.mixin;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Accessor("renderables")
    List<Renderable> getRenderables();

    @Accessor("children")
    List<GuiEventListener> getChildren();

    @Accessor("narratables")
    List<NarratableEntry> getNarratables();

    @Invoker("rebuildWidgets")
    void invokeRebuildWidgets();
}