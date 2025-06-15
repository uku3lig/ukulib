package net.uku3lig.ukulib.config.option.widget;

import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.uku3lig.ukulib.config.option.CheckedOption;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A widget to input text. Based on {@link net.minecraft.client.gui.widget.TextFieldWidget}.
 */
public class TextInputWidget extends ClickableWidget implements Drawable, CheckedOption {
    private static final int VERTICAL_CURSOR_COLOR = 0xffd0d0d0;
    private static final String HORIZONTAL_CURSOR = "_";
    private static final int TEXT_COLOR = 0xffe0e0e0;
    private static final int BORDER_COLOR = 0xffa0a0a0;
    private static final int BACKGROUND_COLOR = 0xff000000;
    private static final int ERROR_COLOR = 0xFFFF0000;

    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    /**
     * The text.
     *
     * @return the text
     */
    @Getter
    private String text = "";
    private boolean selecting;
    /**
     * The index of the leftmost character that is rendered on a screen.
     */
    private int firstCharacterIndex;
    private int selectionStart;
    private int selectionEnd;

    /**
     * The maximum length of the text.
     *
     * @return the maximum length of the text
     */
    @Getter
    private final int maxLength;
    private final String suggestion;
    private final Consumer<String> changedListener;
    private final Predicate<String> textPredicate;

    private final BiFunction<String, Integer, OrderedText> renderTextProvider = (string, index) -> OrderedText.styledForwardsVisitedString(
            string, Style.EMPTY
    );

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
        super(x, y, width, height, Text.of(suggestion));
        this.changedListener = changedListener;
        this.suggestion = suggestion;
        this.textPredicate = textPredicate;
        this.maxLength = maxLength;

        this.setText(initialValue);
        this.setTooltip(Tooltip.of(Text.of(this.suggestion)));
    }

    @Override
    protected MutableText getNarrationMessage() {
        Text message = this.getMessage();
        return Text.translatable("gui.narrate.editBox", message, this.text);
    }

    /**
     * Sets the text.
     *
     * @param text the new text
     */
    public void setText(String text) {
        if (text.length() > this.maxLength) {
            this.text = text.substring(0, this.maxLength);
        } else {
            this.text = text;
        }

        this.setCursorToEnd();
        this.setSelectionEnd(this.selectionStart);
        this.onChanged(text);
    }

    /**
     * Gets the currently selected text, if any.
     *
     * @return the selected text
     */
    public String getSelectedText() {
        int i = Math.min(this.selectionStart, this.selectionEnd);
        int j = Math.max(this.selectionStart, this.selectionEnd);
        return this.text.substring(i, j);
    }

    /**
     * Writes the given text to the current cursor position.
     *
     * @param text the text to write
     */
    public void write(String text) {
        int i = Math.min(this.selectionStart, this.selectionEnd);
        int j = Math.max(this.selectionStart, this.selectionEnd);
        int k = this.maxLength - this.text.length() - (i - j);
        int l = text.length();
        if (k < l) {
            text = text.substring(0, k);
            l = k;
        }

        this.text = new StringBuilder(this.text).replace(i, j, text).toString();
        this.setSelectionStart(i + l);
        this.setSelectionEnd(this.selectionStart);
        this.onChanged(this.text);
    }

    private void onChanged(String newText) {
        if (this.textPredicate.test(newText)) {
            this.changedListener.accept(newText);
        }
    }

    private void erase(int offset) {
        if (Screen.hasControlDown()) {
            this.eraseWords(offset);
        } else {
            this.eraseCharacters(offset);
        }
    }

    /**
     * Erases words depending on the {@code wordOffset}.
     *
     * @param wordOffset the offset
     */
    public void eraseWords(int wordOffset) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.selectionStart) {
                this.write("");
            } else {
                this.eraseCharacters(this.getWordSkipPosition(wordOffset) - this.selectionStart);
            }
        }
    }

    /**
     * Erases characters depending on the {@code characterOffset}.
     *
     * @param characterOffset the offset
     */
    public void eraseCharacters(int characterOffset) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.selectionStart) {
                this.write("");
            } else {
                int i = this.getCursorPosWithOffset(characterOffset);
                int j = Math.min(i, this.selectionStart);
                int k = Math.max(i, this.selectionStart);
                if (j != k) {
                    this.text = new StringBuilder(this.text).delete(j, k).toString();
                    this.setCursor(j);
                }
            }
        }
    }

    /**
     * Gets the position of the word to skip to. Used for deleting words.
     *
     * @param wordOffset the offset
     * @return the position of the word to skip to
     * @see #getWordSkipPosition(int, int)
     */
    public int getWordSkipPosition(int wordOffset) {
        return this.getWordSkipPosition(wordOffset, this.getCursor());
    }

    /**
     * Gets the position of the word to skip to. Used for deleting words.
     *
     * @param wordOffset     the offset
     * @param cursorPosition the cursor position
     * @return the position of the word to skip to
     */
    private int getWordSkipPosition(int wordOffset, int cursorPosition) {
        int i = cursorPosition;
        boolean bl = wordOffset < 0;
        int j = Math.abs(wordOffset);

        for (int k = 0; k < j; ++k) {
            if (!bl) {
                int l = this.text.length();
                i = this.text.indexOf(32, i);
                if (i == -1) {
                    i = l;
                } else {
                    while (i < l && this.text.charAt(i) == ' ') {
                        ++i;
                    }
                }
            } else {
                while (i > 0 && this.text.charAt(i - 1) == ' ') {
                    --i;
                }

                while (i > 0 && this.text.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }

        return i;
    }

    /**
     * Moves the cursor by the specified offset.
     *
     * @param offset the offset
     */
    public void moveCursor(int offset) {
        this.setCursor(this.getCursorPosWithOffset(offset));
    }

    private int getCursorPosWithOffset(int offset) {
        return Util.moveCursor(this.text, this.selectionStart, offset);
    }

    /**
     * Sets the cursor position.
     *
     * @param cursor the cursor position
     */
    public void setCursor(int cursor) {
        this.setSelectionStart(cursor);
        if (!this.selecting) {
            this.setSelectionEnd(this.selectionStart);
        }

        this.onChanged(this.text);
    }

    /**
     * Sets the start of the selection.
     *
     * @param cursor the start of the selection
     */
    public void setSelectionStart(int cursor) {
        this.selectionStart = MathHelper.clamp(cursor, 0, this.text.length());
    }

    /**
     * Sets the cursor position to the start of the text.
     */
    public void setCursorToStart() {
        this.setCursor(0);
    }

    /**
     * Sets the cursor position to the end of the text.
     */
    public void setCursorToEnd() {
        this.setCursor(this.text.length());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.isInactive()) {
            return false;
        } else {
            this.selecting = Screen.hasShiftDown();
            if (Screen.isSelectAll(keyCode)) {
                this.setCursorToEnd();
                this.setSelectionEnd(0);
                return true;
            } else if (Screen.isCopy(keyCode)) {
                MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
                return true;
            } else if (Screen.isPaste(keyCode)) {
                this.write(MinecraftClient.getInstance().keyboard.getClipboard());
                return true;
            } else if (Screen.isCut(keyCode)) {
                MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
                this.write("");
                return true;
            } else {
                switch (keyCode) {
                    case GLFW.GLFW_KEY_BACKSPACE -> {
                        this.selecting = false;
                        this.erase(-1);
                        this.selecting = Screen.hasShiftDown();
                        return true;
                    }
                    case GLFW.GLFW_KEY_DELETE -> {
                        this.selecting = false;
                        this.erase(1);
                        this.selecting = Screen.hasShiftDown();
                        return true;
                    }
                    case GLFW.GLFW_KEY_RIGHT -> {
                        if (Screen.hasControlDown()) {
                            this.setCursor(this.getWordSkipPosition(1));
                        } else {
                            this.moveCursor(1);
                        }
                        return true;
                    }
                    case GLFW.GLFW_KEY_LEFT -> {
                        if (Screen.hasControlDown()) {
                            this.setCursor(this.getWordSkipPosition(-1));
                        } else {
                            this.moveCursor(-1);
                        }
                        return true;
                    }
                    case GLFW.GLFW_KEY_HOME -> {
                        this.setCursorToStart();
                        return true;
                    }
                    case GLFW.GLFW_KEY_END -> {
                        this.setCursorToEnd();
                        return true;
                    }
                    default -> {
                        return false;
                    }
                }
            }
        }
    }

    /**
     * Checks if the text field is inactive.
     *
     * @return {@code true} if the text field is inactive, {@code false} otherwise
     */
    public boolean isInactive() {
        return !this.isVisible() || !this.isFocused();
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (this.isInactive()) {
            return false;
        } else if (chr >= ' ') {
            this.write(Character.toString(chr));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isVisible() && button == 0) {
            this.setFocused(isMouseOver(mouseX, mouseY));

            if (this.isFocused()) {
                int i = MathHelper.floor(mouseX) - this.getX() - 4;
                String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
                this.setCursor(this.textRenderer.trimToWidth(string, i).length() + this.firstCharacterIndex);
                return true;
            }
        }

        return false;
    }

    @Override
    public void renderWidget(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        if (!this.isVisible()) return;

        int borderColor = this.isFocused() ? 0xFFFFFFFF : BORDER_COLOR;
        drawContext.fill(this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1, this.getY() + this.height + 1, borderColor);
        drawContext.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, BACKGROUND_COLOR);

        int cursorStart = this.selectionStart - this.firstCharacterIndex;
        int cursorEnd = this.selectionEnd - this.firstCharacterIndex;
        String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
        boolean isSelectionInBounds = cursorStart >= 0 && cursorStart <= string.length();
        int textX = this.getX() + 4;
        int textY = this.getY() + (this.height - 8) / 2;
        int textEnd = textX;
        if (cursorEnd > string.length()) {
            cursorEnd = string.length();
        }

        int textColor = this.isValid() ? TEXT_COLOR : ERROR_COLOR;

        if (!string.isEmpty()) {
            // render the text before the cursor
            String beforeCursor = isSelectionInBounds ? string.substring(0, cursorStart) : string;
            OrderedText orderedText = this.renderTextProvider.apply(beforeCursor, this.firstCharacterIndex);
            drawContext.drawTextWithShadow(this.textRenderer, orderedText, textX, textY, textColor);
            textEnd += this.textRenderer.getWidth(orderedText) + 1;
        }

        boolean isCursorInTheMiddle = this.selectionStart < this.text.length() || this.text.length() >= this.getMaxLength();
        int cursorX = textEnd;
        if (!isSelectionInBounds) {
            cursorX = cursorStart > 0 ? textX + this.width : textX;
        } else if (isCursorInTheMiddle) {
            cursorX = textEnd - 1;
            --textEnd;
        }

        if (!string.isEmpty() && isSelectionInBounds && cursorStart < string.length()) {
            // render the text after the cursor
            drawContext.drawTextWithShadow(this.textRenderer, this.renderTextProvider.apply(string.substring(cursorStart), this.selectionStart), textEnd, textY, textColor);
        }

        boolean canSuggestionBeRendered = this.textRenderer.getWidth(string + suggestion) < this.getInnerWidth();
        if (!this.suggestion.isBlank() && canSuggestionBeRendered) {
            // render the suggestion (if possible)
            int x = this.getX() + this.getWidth() - 4 - this.textRenderer.getWidth(suggestion);
            drawContext.drawTextWithShadow(this.textRenderer, this.suggestion, x, textY, 0xff808080);
        }

        if (this.isFocused()) {
            if (isCursorInTheMiddle) {
                drawContext.fill(cursorX, textY - 1, cursorX + 1, textY + 1 + 9, VERTICAL_CURSOR_COLOR);
            } else {
                drawContext.drawTextWithShadow(this.textRenderer, HORIZONTAL_CURSOR, cursorX, textY, TEXT_COLOR);
            }
        }

        if (cursorEnd != cursorStart) {
            // render the selection highlight
            int p = textX + this.textRenderer.getWidth(string.substring(0, cursorEnd));
            this.drawSelectionHighlight(drawContext, cursorX, textY - 1, p - 1, textY + 1 + 9);
        }
    }

    private void drawSelectionHighlight(DrawContext drawContext, int x1, int y1, int x2, int y2) {
        if (x1 < x2) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            int i = y1;
            y1 = y2;
            y2 = i;
        }

        if (x2 > this.getX() + this.width) {
            x2 = this.getX() + this.width;
        }

        if (x1 > this.getX() + this.width) {
            x1 = this.getX() + this.width;
        }

        drawContext.fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, x1, y1, x2, y2, Colors.BLUE);
    }

    /**
     * Returns the position of the cursor, or the beginning of the selection.
     *
     * @return the cursor position
     */
    public int getCursor() {
        return this.selectionStart;
    }

    @Nullable
    @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return this.visible ? super.getNavigationPath(navigation) : null;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.visible
                && mouseX >= this.getX()
                && mouseX < (this.getX() + this.width)
                && mouseY >= this.getY()
                && mouseY < (this.getY() + this.height);
    }

    /**
     * Returns width of the text field, without the border.
     *
     * @return the inner width
     */
    public int getInnerWidth() {
        return this.width - 8;
    }

    /**
     * Sets the end of the selection.
     *
     * @param index the end of the selection
     */
    public void setSelectionEnd(int index) {
        int i = this.text.length();
        this.selectionEnd = MathHelper.clamp(index, 0, i);
        if (this.textRenderer != null) {
            if (this.firstCharacterIndex > i) {
                this.firstCharacterIndex = i;
            }

            int j = this.getInnerWidth();
            String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), j);
            int k = string.length() + this.firstCharacterIndex;
            if (this.selectionEnd == this.firstCharacterIndex) {
                this.firstCharacterIndex -= this.textRenderer.trimToWidth(this.text, j, true).length();
            }

            if (this.selectionEnd > k) {
                this.firstCharacterIndex += this.selectionEnd - k;
            } else if (this.selectionEnd <= this.firstCharacterIndex) {
                this.firstCharacterIndex -= this.firstCharacterIndex - this.selectionEnd;
            }

            this.firstCharacterIndex = MathHelper.clamp(this.firstCharacterIndex, 0, i);
        }
    }

    /**
     * Whether the text field is currently visible.
     *
     * @return {@code true} if the text field is visible, {@code false} otherwise
     */
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, this.getNarrationMessage());
    }

    @Override
    public boolean isValid() {
        return this.textPredicate.test(this.text);
    }
}