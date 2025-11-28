package net.uku3lig.ukulib.config.impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
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

    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

    /**
     * Creates the screen.
     *
     * @param parent The parent screen
     */
    public BrokenConfigScreen(Screen parent) {
        super(Text.of("Broken config screen"), parent);
    }

    @Override
    protected void init() {
        super.init();

        this.layout.addHeader(this.title, this.textRenderer);

        DirectionalLayoutWidget body = this.layout.addBody(DirectionalLayoutWidget.vertical().spacing(14));
        body.add(new TextWidget(Text.of("There was an issue with this config screen."), this.textRenderer));
        body.add(new TextWidget(Text.of("Please report this issue to the mod author."), this.textRenderer));

        DirectionalLayoutWidget footer = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        footer.add(ButtonWidget.builder(Text.of("Upload logs to mclo.gs"), button -> this.uploadLogs()).build());
        footer.add(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).build());

        this.layout.forEachChild(this::addDrawableChild);
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
    }

    private void uploadLogs() {
        Path logFile = new File(MinecraftClient.getInstance().runDirectory, "logs/latest.log").toPath();

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

                        MinecraftClient.getInstance().setScreen(new ConfirmLinkScreen(confirmed -> {
                            if (confirmed) Util.getOperatingSystem().open(apiRes.url);
                            this.close();
                        }, apiRes.url, true));
                    } else {
                        log.error("Error while uploading logs to mclo.gs: {}", apiRes.error);
                        Ukutils.sendToast(Text.of("Error while uploading logs"), Text.of(apiRes.error));
                    }
                });
    }

    private record APIResponse(boolean success, String id, String url, String error) {
    }
}
