package net.uku3lig.ukulib.mixin;

import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.text.Text;
import net.minecraft.util.ApiServices;
import net.minecraft.util.Identifier;
import net.uku3lig.ukulib.api.UkulibAPI;
import net.uku3lig.ukulib.config.impl.ModListScreen;
import net.uku3lig.ukulib.config.impl.UkulibConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

/**
 * Mixin for {@link OptionsScreen}.
 */
@Slf4j
@Mixin(OptionsScreen.class)
public class MixinOptionsScreen extends Screen {
    @Unique
    private static final Identifier DEFAULT_ICON = Identifier.of("ukulib", "uku.png");

    @Unique
    private SkinTextures skinTextures = null;

    @Unique
    private ButtonWidget ukulibButton;

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
        fetchSkinTextures(username).thenAccept(t -> this.skinTextures = t);

        // ugly
        ButtonWidget credits = this.children().stream()
                .filter(c -> c instanceof ButtonWidget b && b.getMessage().equals(Text.translatable("options.credits_and_attribution")))
                .map(ButtonWidget.class::cast)
                .findFirst()
                .orElseGet(() -> ButtonWidget.builder(Text.empty(), b -> {
                }).build()); // should never happen

        this.ukulibButton = this.addDrawableChild(
                new ButtonWidget.Builder(Text.empty(), button -> MinecraftClient.getInstance().setScreen(new ModListScreen(this)))
                        .size(20, 20)
                        .position(credits.getRight() + 2, credits.getY())
                        .build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        if (this.skinTextures != null) {
            PlayerSkinDrawer.draw(context, this.skinTextures, this.ukulibButton.getX() + 2, this.ukulibButton.getY() + 2, 16);
        } else {
            // i have to use the long method because idk mojank stuff, drawGuiTexture doesn't work here
            context.drawTexture(RenderPipelines.GUI_TEXTURED, DEFAULT_ICON, this.ukulibButton.getX() + 2, this.ukulibButton.getY() + 2, 0, 0, 16, 16, 16, 16);
        }
    }

    @Unique
    private static CompletableFuture<SkinTextures> fetchSkinTextures(String username) {
        YggdrasilAuthenticationService service = ((MinecraftClientAccessor) MinecraftClient.getInstance()).getAuthenticationService();
        ApiServices services = ApiServices.create(service, MinecraftClient.getInstance().runDirectory);

        return CompletableFuture.supplyAsync(() ->
                        services.profileRepository().findProfileByName(username)
                                .map(p -> services.sessionService().fetchProfile(p.getId(), true))
                                .map(ProfileResult::profile))
                .thenCompose(optProfile -> {
                    if (optProfile.isEmpty()) {
                        log.error("Could not fetch profile {}", username);
                        return null;
                    } else {
                        return MinecraftClient.getInstance().getSkinProvider().fetchSkinTextures(optProfile.get())
                                .thenApply(o -> o.orElse(null));
                    }
                });
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
