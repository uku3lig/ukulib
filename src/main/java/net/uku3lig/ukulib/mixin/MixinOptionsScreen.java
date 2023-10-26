package net.uku3lig.ukulib.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.uku3lig.ukulib.api.UkulibAPI;
import net.uku3lig.ukulib.config.impl.ModListScreen;
import net.uku3lig.ukulib.config.impl.UkulibConfig;
import net.uku3lig.ukulib.utils.IconButton;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Mixin for {@link OptionsScreen}.
 */
@Slf4j
@Mixin(OptionsScreen.class)
public class MixinOptionsScreen extends Screen {
    @Unique
    private static final Identifier DEFAULT_ICON = new Identifier("ukulib", "uku.png");

    @Unique
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    @Unique
    private IconButton ukulibButton;

    /**
     * Adds a button to open the config screen.
     *
     * @param ci callback info
     */
    @Inject(method = "init", at = @At("RETURN"))
    public void addUkulibButton(CallbackInfo ci) {
        if (FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class).isEmpty()) return;
        if (!UkulibConfig.get().isButtonInOptions()) return;

        String username = UkulibConfig.get().getHeadName();
        Identifier texture = DEFAULT_ICON;

        // custom heads cause a crash with vulkanmod, see https://github.com/uku3lig/ukulib/issues/12
        // TODO: custom icon caching?
        if (!FabricLoader.getInstance().isModLoaded("vulkanmod")) {
            Identifier customHeadTex = Ukutils.getHeadTex(username);
            if (Ukutils.textureExists(customHeadTex)) {
                texture = customHeadTex;
            } else {
                registerHeadTex(username).thenRun(() -> this.ukulibButton.setTexture(customHeadTex));
            }
        }

        this.ukulibButton = this.addDrawableChild(new IconButton(this.width / 2 + 158, this.height / 6 + 144 - 6, 20, 20,
                texture, 16, 16,
                button -> MinecraftClient.getInstance().setScreen(new ModListScreen(this))));
    }

    /**
     * <h4>WARNING: INTERNAL METHOD, DO NOT USE !!!!!!!!</h4>
     * Registers the head texture of a player.
     *
     * @param username the username of the player
     * @return a future that completes when the texture is registered
     */
    @Unique
    private static CompletableFuture<Void> registerHeadTex(String username) {
        Identifier texture = Ukutils.getHeadTex(username);
        if (Ukutils.textureExists(texture)) {
            return CompletableFuture.completedFuture(null);
        }

        HttpRequest uuidReq = HttpRequest.newBuilder(URI.create("https://api.mojang.com/users/profiles/minecraft/" + username))
                .GET().build();

        return HTTP_CLIENT.sendAsync(uuidReq, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    if (res.statusCode() != 200) {
                        log.error("Error while getting UUID of " + username + ": " + res.statusCode() + " " + res.body());
                        throw new CompletionException(new NoSuchElementException("Error while getting UUID of " + username));
                    }

                    JsonObject json = new Gson().fromJson(res.body(), JsonObject.class);
                    String uuid = json.get("id").getAsString();

                    TextureManager texManager = MinecraftClient.getInstance().getTextureManager();

                    if (!Ukutils.textureExists(texture)) {
                        try {
                            URL url = new URL("https://crafatar.com/avatars/" + uuid + "?overlay&size=16");
                            try (InputStream stream = url.openStream()) {
                                NativeImage image = NativeImage.read(stream);
                                texManager.registerTexture(texture, new NativeImageBackedTexture(image));
                            }
                        } catch (IOException e) {
                            log.error("Could not fetch head texture", e);
                            throw new CompletionException(e);
                        }
                    }
                });
    }

    /**
     * constructor :D
     *
     * @param title the title of the screen
     */
    protected MixinOptionsScreen(Text title) {
        super(title);
    }
}
