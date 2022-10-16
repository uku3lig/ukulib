package net.uku3lig.ukulib.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.uku3lig.ukulib.api.UkulibAPI;
import net.uku3lig.ukulib.config.impl.UkulibConfigScreen;
import net.uku3lig.ukulib.utils.IconButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for {@link SettingsScreen}.
 */
@Mixin(SettingsScreen.class)
public class MixinSettingsScreen extends Screen {
    private static final Identifier ICON = new Identifier("ukulib", "uku.png");

    /**
     * Adds a button to open the config screen.
     * @param ci callback info
     */
    @Inject(method = "init", at = @At("RETURN"))
    public void addUkulibButton(CallbackInfo ci) {
        if (FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class).isEmpty()) return;

        this.addButton(new IconButton(this.width / 2 + 158, this.height / 6 + 120 - 6, 20, 20, 0, 0, 20, ICON, 20, 20, button -> MinecraftClient.getInstance().openScreen(new UkulibConfigScreen(this))));
    }

    /**
     * @param title the title of the screen
     */
    protected MixinSettingsScreen(Text title) {
        super(title);
    }
}
