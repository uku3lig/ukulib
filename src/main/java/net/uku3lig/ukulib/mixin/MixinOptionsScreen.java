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
import net.uku3lig.ukulib.config.impl.UkulibConfigScreen;
import net.uku3lig.ukulib.utils.IconButton;
import net.uku3lig.ukulib.utils.Ukutils;
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
    private static final Identifier DEFAULT_ICON = new Identifier("ukulib", "uku.png");

    @Unique
    private IconButton ukulibButton;

    /**
     * Adds a button to open the config screen.
     *
     * @param ci callback info
     */
    @Inject(method = "init", at = @At("RETURN"))
    public void addUkulibButton(CallbackInfo ci) {
        if (FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class).isEmpty()) return;
        if (!UkulibConfig.get().isButtonInOptions()) return;

        String username = UkulibConfig.get().getHeadName();
        Identifier texture = Ukutils.getHeadTex(username);
        texture = Ukutils.textureExists(texture) ? texture : DEFAULT_ICON;

        this.ukulibButton = this.addDrawableChild(new IconButton(this.width / 2 + 158, this.height / 6 + 144 - 6, 20, 20,
                texture, 16, 16,
                button -> MinecraftClient.getInstance().setScreen(new ModListScreen(this))));

        UkulibConfigScreen.registerHeadTex(username).thenRun(() -> this.ukulibButton.setTexture(Ukutils.getHeadTex(username)));
    }

    /**
     * constructor :D
     *
     * @param title the title of the screen
     */
    protected MixinOptionsScreen(Text title) {
        super(title);
    }
}
