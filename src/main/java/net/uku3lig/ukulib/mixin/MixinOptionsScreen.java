package net.uku3lig.ukulib.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.uku3lig.ukulib.config.screen.UkulibConfigScreen;
import net.uku3lig.ukulib.utils.IconButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class MixinOptionsScreen extends Screen {
    private static final Identifier ICON = new Identifier("ukulib", "uku.png");

    @Inject(method = "init", at = @At("RETURN"))
    public void addBetterHurtCamButton(CallbackInfo ci) {
        this.addDrawableChild(new IconButton(this.width / 2 + 158, this.height / 6 + 120 - 6, 20, 20, 0, 0, 20, ICON, 20, 20, button -> MinecraftClient.getInstance().setScreen(new UkulibConfigScreen(this))));
    }

    protected MixinOptionsScreen(Text title) {
        super(title);
    }
}
