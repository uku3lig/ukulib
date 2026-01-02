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
import net.minecraft.resources.Identifier;
import net.minecraft.server.Services;
import net.minecraft.world.entity.player.PlayerSkin;
import net.uku3lig.ukulib.config.impl.ModListScreen;
import net.uku3lig.ukulib.config.impl.UkulibConfig;
import net.uku3lig.ukulib.utils.PlatformUkutils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Mixin(OptionsScreen.class)
public class MixinOptionsScreen extends Screen {
    @Unique
    private static final Identifier DEFAULT_ICON = Identifier.fromNamespaceAndPath("ukulib", "uku.png");

    @Unique
    private PlayerSkin playerSkin = null;

    @Unique
    private Button ukulibButton = null;

    @Unique
    private Button creditsButton = null;

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/options/OptionsScreen;repositionElements()V"))
    public void addUkulibButton(CallbackInfo ci) {
        if (PlatformUkutils.INSTANCE.getConfigMods().isEmpty()) return;
        if (!UkulibConfig.get().isButtonInOptions()) return;

        String username = UkulibConfig.get().getHeadName();
        fetchSkinTextures(username).thenAccept(t -> this.playerSkin = t);

        this.creditsButton = this.children().stream()
                .filter(c -> c instanceof Button b && b.getMessage().equals(Component.translatable("options.credits_and_attribution")))
                .map(Button.class::cast)
                .findFirst()
                .orElse(null);

        if (this.creditsButton == null) {
            log.error("Could not find the credits button to align the uku button!");
        }

        this.ukulibButton = this.addRenderableWidget(
                new Button.Builder(Component.empty(), _ -> Minecraft.getInstance().setScreen(new ModListScreen(this)))
                        .size(20, 20)
                        .build()
        );
    }

    @Inject(method = "repositionElements", at = @At("RETURN"))
    public void refreshWidgetPositions(CallbackInfo ci) {
        if (this.ukulibButton != null && this.creditsButton != null) {
            this.ukulibButton.setPosition(this.creditsButton.getRight() + 2, this.creditsButton.getY());
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float deltaTicks) {
        super.render(graphics, mouseX, mouseY, deltaTicks);

        // if no mod provides an ukulib config screen, the button isn't created and causes a NPE
        if (this.ukulibButton != null) {
            if (this.playerSkin != null) {
                PlayerFaceRenderer.draw(graphics, this.playerSkin, this.ukulibButton.getX() + 2, this.ukulibButton.getY() + 2, 16);
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

    protected MixinOptionsScreen(Component title) {
        super(title);
    }
}
