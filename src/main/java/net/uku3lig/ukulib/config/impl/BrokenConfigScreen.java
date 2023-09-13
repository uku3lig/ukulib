package net.uku3lig.ukulib.config.impl;

import gs.mclo.java.APIResponse;
import gs.mclo.java.MclogsAPI;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.uku3lig.ukulib.utils.Ukutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Simple screen shown when a config screen is broken.
 */
@Slf4j
public class BrokenConfigScreen extends Screen {
    private final Screen parent;

    /**
     * Creates the screen.
     * @param parent The parent screen
     */
    public BrokenConfigScreen(Screen parent) {
        super(Text.of("Broken config screen"));
        this.parent = parent;
    }

    static {
        MclogsAPI.mcversion = MinecraftClient.getInstance().getGameVersion();
        MclogsAPI.userAgent = "ukulib";
        MclogsAPI.version = FabricLoader.getInstance().getModContainer("ukulib")
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getVersion)
                .map(Version::getFriendlyString)
                .orElse("unknown");
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);

        drawContext.drawCenteredTextWithShadow(textRenderer, Text.of("There was an issue with this config screen.").asOrderedText(), width / 2, 100, 0xFFFFFF);
        drawContext.drawCenteredTextWithShadow(textRenderer, Text.of("Please report this issue to the mod author.").asOrderedText(), width / 2, 100 + textRenderer.fontHeight + 4, 0xFFFFFF);

        this.addDrawableChild(ButtonWidget.builder(Text.of("Upload logs to mclo.gs"), button -> uploadLogs())
                .dimensions(this.width / 2 - 100, this.height - 51, 200, 20).build());
        this.addDrawableChild(Ukutils.doneButton(this.width, this.height, this.parent));
    }

    private void uploadLogs() {
        Path logFile = new File(MinecraftClient.getInstance().runDirectory, "logs/latest.log").toPath();

        try {
            APIResponse response = MclogsAPI.share(logFile);

            if (!response.success || response.url == null) {
                throw new IOException(response.error);
            } else {
                log.info("Uploaded logs to {}", response.url);

                MinecraftClient.getInstance().setScreen(new ConfirmLinkScreen(confirmed -> {
                    if (confirmed) Util.getOperatingSystem().open(response.url);
                    this.close();
                }, response.url, true));
            }
        } catch (Exception e) {
            log.error("Error while uploading logs to mclo.gs: {}", e.getMessage());
            ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
            SystemToast.show(toastManager, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Error while uploading logs"), Text.of(e.getMessage()));
        }
    }
}
