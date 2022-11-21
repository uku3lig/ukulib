package net.uku3lig.ukulib.config.impl;

import gs.mclo.java.APIResponse;
import gs.mclo.java.MclogsAPI;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmChatLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Simple screen shown when a config screen is broken.
 */
@Log4j2
public class BrokenConfigScreen extends Screen {
    private final Screen parent;

    /**
     * Creates the screen.
     * @param parent The parent screen
     */
    public BrokenConfigScreen(Screen parent) {
        super(new LiteralText("Broken config screen"));
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
    public void onClose() {
        MinecraftClient.getInstance().openScreen(parent);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        drawCenteredString(font, "There was an issue with this config screen.", width / 2, 100, 0xFFFFFF);
        drawCenteredString(font, "Please report this issue to the mod author.", width / 2, 100 + font.fontHeight + 4, 0xFFFFFF);

        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 51, 200, 20, "Upload logs to mclo.gs", button -> uploadLogs()));
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, I18n.translate("gui.done"), button -> this.minecraft.openScreen(this.parent)));
        super.render(mouseX, mouseY, delta);
    }

    private void uploadLogs() {
        Path logFile = new File(MinecraftClient.getInstance().runDirectory, "logs/latest.log").toPath();

        try {
            APIResponse response = MclogsAPI.share(logFile);

            if (!response.success || response.url == null) {
                throw new IOException(response.error);
            } else {
                log.info("Uploaded logs to {}", response.url);

                MinecraftClient.getInstance().openScreen(new ConfirmChatLinkScreen(confirmed -> {
                    if (confirmed) Util.getOperatingSystem().open(response.url);
                    this.onClose();
                }, response.url, true));
            }
        } catch (Exception e) {
            log.error("Error while uploading logs to mclo.gs: {}", e.getMessage());
            ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
            SystemToast.show(toastManager, SystemToast.Type.NARRATOR_TOGGLE, new LiteralText("Error while uploading logs"), new LiteralText(e.getMessage()));
        }
    }
}
