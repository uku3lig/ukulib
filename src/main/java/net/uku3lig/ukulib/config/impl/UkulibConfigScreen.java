package net.uku3lig.ukulib.config.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.uku3lig.ukulib.config.option.CyclingOption;
import net.uku3lig.ukulib.config.option.InputOption;
import net.uku3lig.ukulib.config.option.WidgetCreator;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;
import net.uku3lig.ukulib.utils.Ukutils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

/**
 * ukulib's config screen.
 */
@Slf4j
public class UkulibConfigScreen extends AbstractConfigScreen<UkulibConfig> {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    /**
     * Creates a config screen.
     *
     * @param parent The parent screen
     */
    public UkulibConfigScreen(Screen parent) {
        super("ukulib.config.title", parent, UkulibConfig.getManager());
    }

    @Override
    protected WidgetCreator[] getWidgets(UkulibConfig config) {
        return new WidgetCreator[]{
                CyclingOption.ofBoolean("ukulib.config.buttonInOptions", config.isButtonInOptions(), config::setButtonInOptions),
                CyclingOption.ofBoolean("ukulib.config.modMenuIntegration", config.isModMenuIntegration(), config::setModMenuIntegration),
                new InputOption("ukulib.config.headName", config.getHeadName(), config::setHeadName)
        };
    }

    @Override
    public void removed() {
        super.removed();
        registerHeadTex(UkulibConfig.get().getHeadName());
    }

    private void registerHeadTex(String username) {
        HttpRequest uuidReq = HttpRequest.newBuilder(URI.create("https://api.mojang.com/users/profiles/minecraft/" + username))
                .GET().build();

        HTTP_CLIENT.sendAsync(uuidReq, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    if (res.statusCode() != 200) {
                        log.error("Error while getting UUID of " + username + ": " + res.statusCode() + " " + res.body());
                        return;
                    }

                    JsonObject json = new Gson().fromJson(res.body(), JsonObject.class);
                    String uuid = json.get("id").getAsString();

                    TextureManager texManager = MinecraftClient.getInstance().getTextureManager();
                    Identifier texture = new Identifier("ukulib", "head_" + username.toLowerCase(Locale.ROOT));

                    if (!Ukutils.textureExists(texture)) {
                        try {
                            URL url = new URL("https://crafatar.com/avatars/" + uuid + "?overlay&size=16");
                            try (InputStream stream = url.openStream()) {
                                NativeImage image = NativeImage.read(stream);
                                texManager.registerTexture(texture, new NativeImageBackedTexture(image));
                            }
                        } catch (IOException e) {
                            log.error("Could not fetch head texture", e);
                        }
                    }
                });
    }
}
