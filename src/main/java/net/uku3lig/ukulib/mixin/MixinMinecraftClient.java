package net.uku3lig.ukulib.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * Mixin used to process keybindings.
 *
 * @see Ukutils#registerKeybinding(KeyMapping, Consumer)
 */
@Mixin(Minecraft.class)
public class MixinMinecraftClient {
    /**
     * The awesome injector that processes all the keybindings :3
     *
     * @param ci callback info
     */
    @Inject(method = "tick", at = @At("TAIL"))
    public void processKeybindings(CallbackInfo ci) {
        Ukutils.getKeybindings().forEach((bind, action) -> {
            while (bind.consumeClick()) {
                action.accept(Minecraft.getInstance());
            }
        });
    }

    private MixinMinecraftClient() {}
}
