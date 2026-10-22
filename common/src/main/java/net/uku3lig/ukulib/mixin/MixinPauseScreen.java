package net.uku3lig.ukulib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.uku3lig.ukulib.config.impl.UkulibConfig;
import net.uku3lig.ukulib.utils.PlatformUkutils;
import net.uku3lig.ukulib.utils.UkuButtonRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public class MixinPauseScreen extends Screen {
    @Unique
    private Button ukulib$ukulibButton = null;

    @Inject(method = "createPauseMenu", at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 3, target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"))
    public void addUkuButton(CallbackInfo ci, @Local(name = "iconButtonRow") LinearLayout iconButtonRow) {
        this.ukulib$ukulibButton = null;
        if (PlatformUkutils.INSTANCE.getConfigMods().isEmpty()) return;
        if (!UkulibConfig.get().isButtonInOptions()) return;

        this.ukulib$ukulibButton = UkuButtonRenderer.fetchSkinAndBuild(this);
        iconButtonRow.addChild(ukulib$ukulibButton);
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        UkuButtonRenderer.extract(graphics, this.ukulib$ukulibButton);
    }

    protected MixinPauseScreen(Component title) {
        super(title);
    }
}
