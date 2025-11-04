package net.uku3lig.ukulib.mixin;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Services;
import net.minecraft.world.entity.player.PlayerSkin;
import net.uku3lig.ukulib.Ukulib;
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
    private static final ResourceLocation DEFAULT_ICON = ResourceLocation.fromNamespaceAndPath("ukulib", "uku.png");

    @Unique
    private PlayerSkin skinTextures = null;

    @Unique
    private Button ukulibButton = null;

    /**
     * Adds a button to open the config screen.
     *
     * @param ci callback info
     */
    @Inject(method = "init", at = @At("RETURN"))
    public void addUkulibButton(CallbackInfo ci) {
        if (Ukulib.getConfigMods().isEmpty()) return;
        if (!UkulibConfig.get().isButtonInOptions()) return;

        String username = UkulibConfig.get().getHeadName();
        fetchSkinTextures(username).thenAccept(t -> this.skinTextures = t);

        // ugly
        Button credits = this.children().stream()
                .filter(c -> c instanceof Button b && b.getMessage().equals(Component.translatable("options.credits_and_attribution")))
                .map(Button.class::cast)
                .findFirst()
                .orElseGet(() -> Button.builder(Component.empty(), b -> {
                }).build()); // should never happen

        this.ukulibButton = this.addRenderableWidget(
                new Button.Builder(Component.empty(), button -> Minecraft.getInstance().setScreen(new ModListScreen(this)))
                        .size(20, 20)
                        .pos(credits.getRight() + 2, credits.getY())
                        .build()
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float deltaTicks) {
        super.render(graphics, mouseX, mouseY, deltaTicks);

        // if no mod provides an ukulib config screen, the button isn't created and causes a NPE
        if (this.ukulibButton != null) {
            if (this.skinTextures != null) {
                PlayerFaceRenderer.draw(graphics, this.skinTextures, this.ukulibButton.getX() + 2, this.ukulibButton.getY() + 2, 16);
            } else {
                // i have to use the long method because idk mojank stuff, drawGuiTexture doesn't work here
                graphics.blit(RenderPipelines.GUI_TEXTURED, DEFAULT_ICON, this.ukulibButton.getX() + 2, this.ukulibButton.getY() + 2, 0, 0, 16, 16, 16, 16);
            }
        }
    }

    @Unique
    private static CompletableFuture<PlayerSkin> fetchSkinTextures(String username) {
        Services services = Minecraft.getInstance().services();
        return CompletableFuture.supplyAsync(() -> services.profileResolver().fetchByName(username))
                .thenComposeAsync(optProfile -> {
                    if (optProfile.isEmpty()) {
                        log.error("Could not fetch profile {}", username);
                        return null;
                    } else {
                        return Minecraft.getInstance().getSkinManager().get(optProfile.get())
                                .thenApply(o -> o.orElse(null));
                    }
                });
    }

    /**
     * constructor :D
     *
     * @param title the title of the screen
     */
    protected MixinOptionsScreen(Component title) {
        super(title);
    }
}
