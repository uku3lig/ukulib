package net.uku3lig.ukulib.mixin;

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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

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

        TextureManager texManager = MinecraftClient.getInstance().getTextureManager();
        HttpRequest req = HttpRequest.newBuilder(URI.create("https://mc-heads.net/avatar/" + username + "/16.png")).GET().build();

        return HTTP_CLIENT.sendAsync(req, HttpResponse.BodyHandlers.ofByteArray()).thenAccept(r -> {
            if (r.statusCode() == 200) {
                try {
                    NativeImage image = NativeImage.read(r.body());
                    texManager.registerTexture(texture, new NativeImageBackedTexture(image));
                } catch (IOException e) {
                    log.error("Failed to register head texture", e);
                }
            } else {
                log.error("Could not fetch head texture: {} {}", r.statusCode(), new String(r.body()));
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
