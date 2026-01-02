package net.uku3lig.ukulib.mixin;

import net.minecraft.client.Minecraft;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "tick", at = @At("TAIL"))
    public void processKeybindings(CallbackInfo ci) {
        Ukutils.getKeybindings().forEach((bind, action) -> {
            while (bind.consumeClick()) {
                action.accept(Minecraft.getInstance());
            }
        });
    }
}
