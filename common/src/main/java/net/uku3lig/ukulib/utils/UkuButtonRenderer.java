package net.uku3lig.ukulib.utils;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.Services;
import net.minecraft.world.entity.player.PlayerSkin;
import net.uku3lig.ukulib.config.impl.ModListScreen;
import net.uku3lig.ukulib.config.impl.UkulibConfig;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class UkuButtonRenderer {
    private static final Identifier DEFAULT_ICON = Identifier.fromNamespaceAndPath("ukulib", "uku.png");
    private static PlayerSkin playerSkin;

    public static Button fetchSkinAndBuild(Screen parent) {
        String username = UkulibConfig.get().getHeadName();
        fetchSkinTextures(username).thenAccept(t -> playerSkin = t);

        return new Button.Builder(Component.empty(), _ -> Minecraft.getInstance().gui.setScreen(new ModListScreen(parent)))
                .size(20, 20)
                .build();
    }

    public static void extract(GuiGraphicsExtractor graphics, AbstractWidget button) {
        extract(graphics, button, 0xFFFFFFFF);
    }

    public static void extract(GuiGraphicsExtractor graphics, @Nullable AbstractWidget button, int color) {
        // if no mod provides an ukulib config screen, the button isn't created and causes a NPE
        if (button != null) {
            if (playerSkin != null) {
                PlayerFaceExtractor.extractRenderState(graphics, playerSkin, button.getX() + 2, button.getY() + 2, 16, color);
            } else {
                // i have to use the long method because idk mojank stuff, drawGuiTexture doesn't work here
                graphics.blit(RenderPipelines.GUI_TEXTURED, DEFAULT_ICON, button.getX() + 2, button.getY() + 2, 0, 0, 16, 16, 16, 16, color);
            }
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
