package net.uku3lig.ukulib.mixin;

import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Mixin used for {@link Ukutils#createButton(String, Consumer)} and {@link Ukutils#createOpenButton(String, UnaryOperator)}
 */
@Mixin(CyclingButtonWidget.Builder.class)
public class MixinCyclingButtonWidgetBuilder<T> {
    @Shadow private int initialIndex;
    @Shadow private CyclingButtonWidget.Values<T> values;
    @Shadow @Final private Function<T, Text> valueToText;
    @Shadow private Function<CyclingButtonWidget<T>, MutableText> narrationMessageFactory;
    @Shadow private CyclingButtonWidget.TooltipFactory<T> tooltipFactory;
    @Shadow private boolean optionTextOmitted;

    @Inject(method = "build(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/CyclingButtonWidget$UpdateCallback;)Lnet/minecraft/client/gui/widget/CyclingButtonWidget;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/CyclingButtonWidget;<init>(IIIILnet/minecraft/text/Text;Lnet/minecraft/text/Text;ILjava/lang/Object;Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Values;Ljava/util/function/Function;Ljava/util/function/Function;Lnet/minecraft/client/gui/widget/CyclingButtonWidget$UpdateCallback;Lnet/minecraft/client/gui/widget/CyclingButtonWidget$TooltipFactory;Z)V"),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void fixText(int x, int y, int width, int height, Text optionText, CyclingButtonWidget.UpdateCallback<T> callback, CallbackInfoReturnable<CyclingButtonWidget<T>> cir, List<T> list, T object, Text text, Text text2) {
        if (text2.getString().contains(Ukutils.BUTTON_PLACEHOLDER.getString())) text2 = optionText;

        CyclingButtonWidget<T> w = new CyclingButtonWidget<>(x, y, width, height, text2, optionText, this.initialIndex, object, this.values, this.valueToText, this.narrationMessageFactory, callback, this.tooltipFactory, this.optionTextOmitted);
        cir.setReturnValue(w);
    }

    private MixinCyclingButtonWidgetBuilder() {
    }
}
