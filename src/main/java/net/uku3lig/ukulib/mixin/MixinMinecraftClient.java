package net.uku3lig.ukulib.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * Mixin used to process keybindings.
 *
 * @see Ukutils#registerKeybinding(KeyBinding, Consumer)
 */
@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    /**
     * The awesome injector that processes all the keybindings :3
     *
     * @param ci callback info
     */
    @Inject(method = "tick", at = @At("TAIL"))
    public void processKeybindings(CallbackInfo ci) {
        Ukutils.getKeybindings().forEach((bind, action) -> {
            while (bind.wasPressed()) {
                action.accept(MinecraftClient.getInstance());
            }
        });
    }

    private MixinMinecraftClient() {}
}
