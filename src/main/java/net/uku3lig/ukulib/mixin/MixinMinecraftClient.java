package net.uku3lig.ukulib.mixin;

import net.minecraft.client.MinecraftClient;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
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
