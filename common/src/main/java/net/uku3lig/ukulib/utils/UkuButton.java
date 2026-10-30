package net.uku3lig.ukulib.utils;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiPipeline;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.Services;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.PlayerSkin;
import net.uku3lig.ukulib.config.impl.ModListScreen;
import net.uku3lig.ukulib.config.impl.UkulibConfig;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class UkuButton extends Button {
    public static final Identifier DEFAULT_ICON = Identifier.fromNamespaceAndPath("ukulib", "uku.png");
    private static PlayerSkin playerSkin;

    public UkuButton(Screen parent) {
        super(0, 0, 20, 20, Component.empty(), _ -> Minecraft.getInstance().gui.setScreen(new ModListScreen(parent)), Button.DEFAULT_NARRATION);
        fetchSkinTextures(UkulibConfig.get().getHeadName()).thenAccept(t -> playerSkin = t);
    }

    @Override
    protected void extractContents(@NonNull GuiGraphicsExtractor graphics) {
        this.extractDefaultSprite(graphics);
        if (playerSkin != null) {
            PlayerFaceExtractor.extractRenderState(graphics, playerSkin, this.getX() + 2, this.getY() + 2, 16, ARGB.white(this.alpha));
        } else {
            // i have to use the long method because idk mojank stuff, drawGuiTexture doesn't work here
            graphics.blit(GuiPipeline.TEXTURED, DEFAULT_ICON, this.getX() + 2, this.getY() + 2, 0, 0, 16, 16, 16, 16, ARGB.white(this.alpha));
        }
    }

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
}
