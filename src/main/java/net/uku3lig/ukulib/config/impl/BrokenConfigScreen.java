package net.uku3lig.ukulib.config.impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import net.uku3lig.ukulib.config.screen.CloseableScreen;
import net.uku3lig.ukulib.utils.Ukutils;

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

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    /**
     * Creates the screen.
     *
     * @param parent The parent screen
     */
    public BrokenConfigScreen(Screen parent) {
        super(Component.translatable("ukulib.brokenConfig.title"), parent);
    }

    @Override
    protected void init() {
        super.init();

        this.layout.addTitleHeader(this.title, this.font);

        LinearLayout body = this.layout.addToContents(LinearLayout.vertical().spacing(14));
        body.addChild(new StringWidget(Component.translatable("ukulib.brokenConfig.line1"), this.font));
        body.addChild(new StringWidget(Component.translatable("ukulib.brokenConfig.line2"), this.font));

        LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footer.addChild(Button.builder(Component.translatable("ukulib.brokenConfig.upload"), button -> this.uploadLogs()).build());
        footer.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).build());

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
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
                        Ukutils.sendToast(Component.translatable("ukulib.brokenConfig.uploadError"), Component.literal(apiRes.error));
                    }
                });
    }

    private record APIResponse(boolean success, String id, String url, String error) {
    }
}
