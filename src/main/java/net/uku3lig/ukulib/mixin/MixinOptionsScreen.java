package net.uku3lig.ukulib.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.screen.UkulibConfigScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class MixinOptionsScreen extends Screen {
    @Inject(method = "init", at = @At("RETURN"))
    public void addBetterHurtCamButton(CallbackInfo ci) {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 24 - 6, 150, 20,
                Text.of("Ukulib Mods..."), button -> MinecraftClient.getInstance().setScreen(new UkulibConfigScreen(this))));
    }

    protected MixinOptionsScreen(Text title) {
        super(title);
    }
}
