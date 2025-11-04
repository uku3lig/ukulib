package net.uku3lig.ukulib.config.impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.uku3lig.ukulib.Ukulib;
import net.uku3lig.ukulib.config.screen.CloseableScreen;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple screen shown when a config screen is broken.
 */
@Slf4j
public class BrokenConfigScreen extends CloseableScreen {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();
    private static final URI API_URL = URI.create("https://api.mclo.gs/1/log");

    /**
     * Creates the screen.
     *
     * @param parent The parent screen
     */
    public BrokenConfigScreen(Screen parent) {
        super(Component.literal("Broken config screen"), parent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.drawCenteredString(font, Component.literal("There was an issue with this config screen."), width / 2, 100, 0xFFFFFFFF);
        graphics.drawCenteredString(font, Component.literal("Please report this issue to the mod author."), width / 2, 100 + font.lineHeight + 4, 0xFFFFFFFF);

        this.addRenderableWidget(Button.builder(Component.literal("Upload logs to mclo.gs"), button -> uploadLogs())
                .bounds(this.width / 2 - 100, this.height - 51, 200, 20).build());
        this.addRenderableWidget(Ukulib.getUtils().doneButton(this.width, this.height, this.parent));
    }

    private void uploadLogs() {
        Path logFile = new File(Minecraft.getInstance().gameDirectory, "logs/latest.log").toPath();

        String content;
        try {
            content = Files.readString(logFile);
        } catch (IOException e) {
            log.error("Failed to read log file", e);
            return;
        }

        HttpRequest request = HttpRequest.newBuilder(API_URL)
                .POST(HttpRequest.BodyPublishers.ofString("content=" + content))
                .header("User-Agent", "uku3lig/ukulib")
                .build();

        HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    APIResponse apiRes = GSON.fromJson(res.body(), APIResponse.class);

                    if (apiRes.success && apiRes.url != null) {
                        log.info("Uploaded logs to {}", apiRes.url);

                        Minecraft.getInstance().setScreen(new ConfirmLinkScreen(confirmed -> {
                            if (confirmed) Util.getPlatform().openUri(apiRes.url);
                            this.onClose();
                        }, apiRes.url, true));
                    } else {
                        log.error("Error while uploading logs to mclo.gs: {}", apiRes.error);
                        Ukulib.getUtils().sendToast(Component.literal("Error while uploading logs"), Component.literal(apiRes.error));
                    }
                });
    }

    private record APIResponse(boolean success, String id, String url, String error) {
    }
}
