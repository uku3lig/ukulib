package net.uku3lig.ukulib.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Slf4j
@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    @Shadow
    protected abstract int getHorizontalPosition(int currentButton, int numberOfButtons, int buttonWidth);

    @Unique
    private Button ukulibButton = null;

    @Definition(id = "numberOfButtons", local = @Local(type = int.class, name = "numberOfButtons"))
    @Expression("numberOfButtons = ?")
    @Inject(method = "init", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER))
    private void adjustAmountOfIconButtons(CallbackInfo ci, @Local(name = "numberOfButtons") LocalIntRef numberOfButtons) {
        if (!PlatformUkutils.INSTANCE.getConfigMods().isEmpty() && UkulibConfig.get().isButtonInOptions()) {
            numberOfButtons.set(numberOfButtons.get() + 1);
        }
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;getHorizontalPosition(III)I"))
    private int fixButtonCount(TitleScreen instance, int currentButton, int wrongNb, int buttonWidth, Operation<Integer> original, @Local(name = "numberOfButtons") int actualNb) {
        return original.call(instance, currentButton, actualNb, buttonWidth);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 1, target = "Lnet/minecraft/client/gui/components/SpriteIconButton;setPosition(II)V"))
    public void addUkuButton(CallbackInfo ci, @Local(name = "numberOfButtons") int numberOfButtons, @Local(name = "currentButton") LocalIntRef currentButton, @Local(name = "topPos") int topPos) {
        this.ukulibButton = null;
        if (PlatformUkutils.INSTANCE.getConfigMods().isEmpty()) return;
        if (!UkulibConfig.get().isButtonInOptions()) return;

        currentButton.set(currentButton.get() + 1);

        this.ukulibButton = this.addRenderableWidget(UkuButtonRenderer.fetchSkinAndBuild(this));
        this.ukulibButton.setPosition(this.getHorizontalPosition(currentButton.get(), numberOfButtons, 20), topPos);
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci, @Local(name = "widgetFade") float widgetFade) {
        UkuButtonRenderer.extract(graphics, this.ukulibButton, ARGB.white(widgetFade));
    }

    protected MixinTitleScreen(Component title) {
        super(title);
    }
}
