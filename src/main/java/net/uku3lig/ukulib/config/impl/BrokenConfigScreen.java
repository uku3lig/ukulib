package net.uku3lig.ukulib.config.impl;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
public class BrokenConfigScreen extends Screen {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String MCLOGS_URL = "https://api.mclo.gs/1/log";

    private final Screen parent;

    public BrokenConfigScreen(Screen parent) {
        super(Text.of("Broken config screen"));
        this.parent = parent;
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredTextWithShadow(matrices, textRenderer, Text.of("There was an issue with this config screen.").asOrderedText(), width / 2, 100, 0xFFFFFF);
        drawCenteredTextWithShadow(matrices, textRenderer, Text.of("Please report this issue to the mod author.").asOrderedText(), width / 2, 100 + textRenderer.fontHeight + 4, 0xFFFFFF);

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 51, 200, 20, Text.of("Upload logs to mclo.gs"), button -> uploadLogs()));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void uploadLogs() {
        File logFile = new File(MinecraftClient.getInstance().runDirectory, "logs/latest.log");
        try (InputStream in = new BufferedInputStream(new FileInputStream(logFile))) {
            String body = "content=" + URLEncoder.encode(IOUtils.toString(in, StandardCharsets.UTF_8), StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URL(MCLOGS_URL).toURI())
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            McLogsResult result = new Gson().fromJson(response.body(), McLogsResult.class);

            if (response.statusCode() >= 400 || !result.isSuccess() || result.getUrl() == null) {
                log.error("Error while uploading logs to mclo.gs: {}", result.getError());
                ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
                SystemToast.show(toastManager, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Error while uploading logs"), Text.of(result.getError()));
            } else {
                log.info("Uploaded logs to {}", result.getUrl());

                MinecraftClient.getInstance().setScreen(new ConfirmLinkScreen(confirmed -> {
                    if (confirmed) Util.getOperatingSystem().open(result.getUrl());
                    this.close();
                }, result.getUrl(), true));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Error while uploading logs", e);
        }
    }

    @Data
    private static class McLogsResult {
        private boolean success;
        private String error;
        private String url;
    }
}
