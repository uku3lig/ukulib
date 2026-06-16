package net.uku3lig.ukulib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.uku3lig.ukulib.config.impl.UkulibConfig;
import net.uku3lig.ukulib.utils.PlatformUkutils;
import net.uku3lig.ukulib.utils.UkuButtonRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Slf4j
@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    @Shadow
    protected abstract int getHorizontalPosition(int currentButton, int numberOfButtons, int buttonWidth);

    @Unique
    private Button ukulibButton = null;

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;getHorizontalPosition(III)I"))
    public int increaseButtonCount(TitleScreen instance, int currentButton, int numberOfButtons, int buttonWidth, Operation<Integer> original) {
        // we add +1 to account for the ukulib button
        return original.call(instance, currentButton, numberOfButtons + 1, buttonWidth);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 1, target = "Lnet/minecraft/client/gui/components/SpriteIconButton;setPosition(II)V"))
    public void addUkuButton(CallbackInfo ci, @Local(name = "numberOfButtons") int numberOfButtons, @Local(name = "currentButton") int currentButton, @Local(name = "topPos") int topPos) {
        if (PlatformUkutils.INSTANCE.getConfigMods().isEmpty()) return;
        if (!UkulibConfig.get().isButtonInOptions()) return;

        this.ukulibButton = this.addRenderableWidget(UkuButtonRenderer.fetchSkinAndBuild(this));
        this.ukulibButton.setPosition(this.getHorizontalPosition(++currentButton, numberOfButtons + 1, 20), topPos);
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci, @Local(name = "widgetFade") float widgetFade) {
        UkuButtonRenderer.extract(graphics, this.ukulibButton, ARGB.white(widgetFade));
    }

    protected MixinTitleScreen(Component title) {
        super(title);
    }
}
