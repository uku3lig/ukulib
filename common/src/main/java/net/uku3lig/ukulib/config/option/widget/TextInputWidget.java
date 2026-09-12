package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.uku3lig.ukulib.config.option.CheckedOption;
import net.uku3lig.ukulib.mixin.EditBoxAccessor;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A widget to input text. Based on {@link net.minecraft.client.gui.components.EditBox}.
 */
public class TextInputWidget extends EditBox implements CheckedOption {
    private static final int TEXT_COLOR = 0xFFE0E0E0;
    private static final int INVALID_COLOR = 0xFFFF0000;

    private final Font font;
    private final String suggestion;
    private final Predicate<String> textPredicate;

    /**
     * Constructor.
     *
     * @param x               the x position
     * @param y               the y position
     * @param width           the width
     * @param height          the height
     * @param initialValue    the initial value
     * @param changedListener the callback to set the value
     * @param suggestion      the suggestion
     * @param textPredicate   the predicate to check the text
     * @param maxLength       the maximum length of the text
     */
    public TextInputWidget(int x, int y, int width, int height, String initialValue, Consumer<String> changedListener, String suggestion, Predicate<String> textPredicate, int maxLength) {
        super(Minecraft.getInstance().font, x, y, width, height, Component.literal(suggestion));
        this.font = Minecraft.getInstance().font;
        this.suggestion = suggestion;
        this.textPredicate = textPredicate;

        this.setValue(initialValue);
        this.setTooltip(Tooltip.create(Component.literal(this.suggestion)));
        this.setMaxLength(maxLength);

        this.setResponder(value -> {
            if (changedListener != null && this.textPredicate.test(value)) {
                changedListener.accept(value);
            }
        });
    }

    @Override
    public boolean isValid() {
        return this.textPredicate.test(this.getValue());
    }

    @Override
    public void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        this.setTextColor(this.isValid() ? TEXT_COLOR : INVALID_COLOR);
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);

        EditBoxAccessor accessor = (EditBoxAccessor) this;

        boolean canSuggestionBeRendered = this.font.width(this.getValue() + suggestion) < this.getInnerWidth();
        if (!this.suggestion.isBlank() && canSuggestionBeRendered) {
            // render the suggestion (if possible)
            int x = this.getX() + this.getWidth() - 4 - this.font.width(suggestion);
            graphics.text(this.font, this.suggestion, x, accessor.getTextY(), CommonColors.GRAY, accessor.getTextShadow());
        }
    }
}
