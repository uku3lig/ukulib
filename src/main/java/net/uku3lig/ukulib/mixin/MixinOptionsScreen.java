package net.uku3lig.ukulib.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.uku3lig.ukulib.api.UkulibAPI;
import net.uku3lig.ukulib.config.impl.ModListScreen;
import net.uku3lig.ukulib.config.impl.UkulibConfig;
import net.uku3lig.ukulib.utils.IconButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for {@link OptionsScreen}.
 */
@Mixin(OptionsScreen.class)
public class MixinOptionsScreen extends Screen {
    @Unique
    private static final Identifier ICON = new Identifier("ukulib", "uku.png");

    /**
     * Adds a button to open the config screen.
     * @param ci callback info
     */
    @Inject(method = "init", at = @At("RETURN"))
    public void addUkulibButton(CallbackInfo ci) {
        if (FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class).isEmpty()) return;
        if (!UkulibConfig.getManager().getConfig().isButtonInOptions()) return;

        this.addDrawableChild(new IconButton(this.width / 2 + 158, this.height / 6 + 144 - 6, 20, 20, 0, 0, 20, ICON, 20, 20, button -> MinecraftClient.getInstance().setScreen(new ModListScreen(this))));
    }

    /**
     * constructor :D
     * @param title the title of the screen
     */
    protected MixinOptionsScreen(Text title) {
        super(title);
    }
}
