package net.uku3lig.ukulib.config.option.widget;

import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.uku3lig.ukulib.config.option.CheckedOption;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A widget to input text. Based on {@link net.minecraft.client.gui.widget.TextFieldWidget}.
 */
// changes from vanilla: suggestion rendering & implements CheckedOption & renders invalid text
public class TextInputWidget extends ClickableWidget implements CheckedOption {
    private static final ButtonTextures TEXTURES = new ButtonTextures(
            Identifier.ofVanilla("widget/text_field"), Identifier.ofVanilla("widget/text_field_highlighted")
    );
    private static final String HORIZONTAL_CURSOR = "_";
    private static final int TEXT_COLOR = 0xFFE0E0E0;
    private static final int INVALID_COLOR = 0xFFFF0000;
    private final TextRenderer textRenderer;
    /**
     * The text.
     *
     * @return the text
     */
    @Getter
    private String text = "";
    private final int maxLength;
    private final boolean drawsBackground = true;
    /**
     * The index of the leftmost character that is rendered on a screen.
     */
    private int firstCharacterIndex;
    private int selectionStart;
    private int selectionEnd;
    private final String suggestion;
    private final Consumer<String> changedListener;
    private final Predicate<String> textPredicate;
    private long lastSwitchFocusTime = Util.getMeasuringTimeMs();
    private int textX;
    private int textY;

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
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.changedListener = changedListener;
        this.suggestion = suggestion;
        this.textPredicate = textPredicate;
        this.maxLength = maxLength;

        this.setText(initialValue);
        this.setTooltip(Tooltip.of(Text.of(this.suggestion)));
        this.updateTextPosition();
    }

    @Override
    protected MutableText getNarrationMessage() {
        Text text = this.getMessage();
        return Text.translatable("gui.narrate.editBox", text, this.text);
    }

    public void setText(String text) {
        if (text.length() > this.maxLength) {
            this.text = text.substring(0, this.maxLength);
        } else {
            this.text = text;
        }

        this.setCursorToEnd(false);
        this.setSelectionEnd(this.selectionStart);
        this.onChanged(text);
    }

    public String getSelectedText() {
        int i = Math.min(this.selectionStart, this.selectionEnd);
        int j = Math.max(this.selectionStart, this.selectionEnd);
        return this.text.substring(i, j);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        this.updateTextPosition();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        this.updateTextPosition();
    }

    public void write(String text) {
        int i = Math.min(this.selectionStart, this.selectionEnd);
        int j = Math.max(this.selectionStart, this.selectionEnd);
        int k = this.maxLength - this.text.length() - (i - j);
        if (k > 0) {
            String string = StringHelper.stripInvalidChars(text);
            int l = string.length();
            if (k < l) {
                if (Character.isHighSurrogate(string.charAt(k - 1))) {
                    k--;
                }

                string = string.substring(0, k);
                l = k;
            }

            this.text = new StringBuilder(this.text).replace(i, j, string).toString();
            this.setSelectionStart(i + l);
            this.setSelectionEnd(this.selectionStart);
            this.onChanged(this.text);
        }
    }

    private void onChanged(String newText) {
        if (this.changedListener != null && this.textPredicate.test(newText)) {
            this.changedListener.accept(newText);
        }

        this.updateTextPosition();
    }

    private void erase(int offset, boolean bl) {
        if (bl) {
            this.eraseWords(offset);
        } else {
            this.eraseCharacters(offset);
        }
    }

    public void eraseWords(int wordOffset) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.selectionStart) {
                this.write("");
            } else {
                this.eraseCharactersTo(this.getWordSkipPosition(wordOffset));
            }
        }
    }

    public void eraseCharacters(int characterOffset) {
        this.eraseCharactersTo(this.getCursorPosWithOffset(characterOffset));
    }

    public void eraseCharactersTo(int position) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.selectionStart) {
                this.write("");
            } else {
                int i = Math.min(position, this.selectionStart);
                int j = Math.max(position, this.selectionStart);
                if (i != j) {
                    this.text = new StringBuilder(this.text).delete(i, j).toString();
                    this.setCursor(i, false);
                }
            }
        }
    }

    public int getWordSkipPosition(int wordOffset) {
        return this.getWordSkipPosition(wordOffset, this.getCursor());
    }

    private int getWordSkipPosition(int wordOffset, int cursorPosition) {
        int i = cursorPosition;
        boolean bl = wordOffset < 0;
        int j = Math.abs(wordOffset);

        for (int k = 0; k < j; k++) {
            if (!bl) {
                int l = this.text.length();
                i = this.text.indexOf(32, i);
                if (i == -1) {
                    i = l;
                } else {
                    while (i < l && this.text.charAt(i) == ' ') {
                        i++;
                    }
                }
            } else {
                while (i > 0 && this.text.charAt(i - 1) == ' ') {
                    i--;
                }

                while (i > 0 && this.text.charAt(i - 1) != ' ') {
                    i--;
                }
            }
        }

        return i;
    }

    public void moveCursor(int offset, boolean shiftKeyPressed) {
        this.setCursor(this.getCursorPosWithOffset(offset), shiftKeyPressed);
    }

    private int getCursorPosWithOffset(int offset) {
        return Util.moveCursor(this.text, this.selectionStart, offset);
    }

    public void setCursor(int cursor, boolean select) {
        this.setSelectionStart(cursor);
        if (!select) {
            this.setSelectionEnd(this.selectionStart);
        }

        this.onChanged(this.text);
    }

    public void setSelectionStart(int cursor) {
        this.selectionStart = MathHelper.clamp(cursor, 0, this.text.length());
        this.updateFirstCharacterIndex(this.selectionStart);
    }

    public void setCursorToStart(boolean shiftKeyPressed) {
        this.setCursor(0, shiftKeyPressed);
    }

    public void setCursorToEnd(boolean shiftKeyPressed) {
        this.setCursor(this.text.length(), shiftKeyPressed);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (this.isInteractable() && this.isFocused()) {
            switch (input.key()) {
                case GLFW.GLFW_KEY_BACKSPACE -> {
                    this.erase(-1, input.hasCtrl());
                    return true;
                }
                case GLFW.GLFW_KEY_DELETE -> {
                    this.erase(1, input.hasCtrl());
                    return true;
                }
                case GLFW.GLFW_KEY_RIGHT -> {
                    if (input.hasCtrl()) {
                        this.setCursor(this.getWordSkipPosition(1), input.hasShift());
                    } else {
                        this.moveCursor(1, input.hasShift());
                    }

                    return true;
                }
                case GLFW.GLFW_KEY_LEFT -> {
                    if (input.hasCtrl()) {
                        this.setCursor(this.getWordSkipPosition(-1), input.hasShift());
                    } else {
                        this.moveCursor(-1, input.hasShift());
                    }

                    return true;
                }
                case GLFW.GLFW_KEY_HOME -> {
                    this.setCursorToStart(input.hasShift());
                    return true;
                }
                case GLFW.GLFW_KEY_END -> {
                    this.setCursorToEnd(input.hasShift());
                    return true;
                }
                default -> {
                    if (input.isSelectAll()) {
                        this.setCursorToEnd(false);
                        this.setSelectionEnd(0);
                        return true;
                    } else if (input.isCopy()) {
                        MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
                        return true;
                    } else if (input.isPaste()) {
                        this.write(MinecraftClient.getInstance().keyboard.getClipboard());
                        return true;
                    } else {
                        if (input.isCut()) {
                            MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
                            this.write("");
                            return true;
                        }

                        return false;
                    }
                }
            }
        } else {
            return false;
        }
    }

    public boolean isActive() {
        return this.isInteractable() && this.isFocused();
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (!this.isActive()) {
            return false;
        } else if (input.isValidChar()) {
            this.write(input.asString());
            return true;
        } else {
            return false;
        }
    }

    private int getClickPosition(Click click) {
        int i = Math.min(MathHelper.floor(click.x()) - this.textX, this.getInnerWidth());
        String string = this.text.substring(this.firstCharacterIndex);
        return this.firstCharacterIndex + this.textRenderer.trimToWidth(string, i).length();
    }

    private void onDoubleClick(Click click) {
        int i = this.getClickPosition(click);
        int j = this.getWordSkipPosition(-1, i);
        int k = this.getWordSkipPosition(1, i);
        this.setCursor(j, false);
        this.setCursor(k, true);
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        if (doubled) {
            this.onDoubleClick(click);
        } else {
            this.setCursor(this.getClickPosition(click), click.hasShift());
        }
    }

    @Override
    protected void onDrag(Click click, double d, double e) {
        this.setCursor(this.getClickPosition(click), true);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        if (this.isVisible()) {
            if (this.drawsBackground()) {
                Identifier identifier = TEXTURES.get(this.isInteractable(), this.isFocused());
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier, this.getX(), this.getY(), this.getWidth(), this.getHeight());
            }

            int i = this.isValid() ? TEXT_COLOR : INVALID_COLOR;
            int j = this.selectionStart - this.firstCharacterIndex;
            String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
            boolean bl = j >= 0 && j <= string.length();
            boolean bl2 = this.isFocused() && (Util.getMeasuringTimeMs() - this.lastSwitchFocusTime) / 300L % 2L == 0L && bl;
            int k = this.textX;
            int l = MathHelper.clamp(this.selectionEnd - this.firstCharacterIndex, 0, string.length());
            boolean textShadow = true;
            if (!string.isEmpty()) {
                String string2 = bl ? string.substring(0, j) : string;
                OrderedText orderedText = this.format(string2);
                context.drawText(this.textRenderer, orderedText, k, this.textY, i, textShadow);
                k += this.textRenderer.getWidth(orderedText) + 1;
            }

            boolean bl3 = this.selectionStart < this.text.length() || this.text.length() >= this.getMaxLength();
            int m = k;
            if (!bl) {
                m = j > 0 ? this.textX + this.width : this.textX;
            } else if (bl3) {
                m = k - 1;
                k--;
            }

            if (!string.isEmpty() && bl && j < string.length()) {
                context.drawText(this.textRenderer, this.format(string.substring(j)), k, this.textY, i, textShadow);
            }

            boolean canSuggestionBeRendered = this.textRenderer.getWidth(string + suggestion) < this.getInnerWidth();
            if (!this.suggestion.isBlank() && canSuggestionBeRendered) {
                // render the suggestion (if possible)
                int x = this.getX() + this.getWidth() - 4 - this.textRenderer.getWidth(suggestion);
                context.drawText(this.textRenderer, this.suggestion, x, this.textY, Colors.GRAY, textShadow);
            }

            if (l != j) {
                int n = this.textX + this.textRenderer.getWidth(string.substring(0, l));
                context.drawSelection(Math.min(m, this.getX() + this.width), this.textY - 1, Math.min(n - 1, this.getX() + this.width), this.textY + 1 + 9, true);
            }

            if (bl2) {
                if (bl3) {
                    context.fill(m, this.textY - 1, m + 1, this.textY + 1 + 9, i);
                } else {
                    context.drawText(this.textRenderer, HORIZONTAL_CURSOR, m, this.textY, i, textShadow);
                }
            }

            if (this.isHovered()) {
                context.setCursor(StandardCursors.IBEAM);
            }
        }
    }

    private OrderedText format(String string) {
        return OrderedText.styledForwardsVisitedString(string, Style.EMPTY);
    }

    private void updateTextPosition() {
        if (this.textRenderer != null) {
            String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
            this.textX = this.getX() + (this.isCentered() ? (this.getWidth() - this.textRenderer.getWidth(string)) / 2 : (this.drawsBackground ? 4 : 0));
            this.textY = this.drawsBackground ? this.getY() + (this.height - 8) / 2 : this.getY();
        }
    }

    private int getMaxLength() {
        return this.maxLength;
    }

    public int getCursor() {
        return this.selectionStart;
    }

    public boolean drawsBackground() {
        return this.drawsBackground;
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (focused) {
            this.lastSwitchFocusTime = Util.getMeasuringTimeMs();
        }
    }

    private boolean isCentered() {
        return false;
    }

    public int getInnerWidth() {
        return this.drawsBackground() ? this.width - 8 : this.width;
    }

    public void setSelectionEnd(int index) {
        this.selectionEnd = MathHelper.clamp(index, 0, this.text.length());
        this.updateFirstCharacterIndex(this.selectionEnd);
    }

    private void updateFirstCharacterIndex(int cursor) {
        if (this.textRenderer != null) {
            this.firstCharacterIndex = Math.min(this.firstCharacterIndex, this.text.length());
            int i = this.getInnerWidth();
            String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), i);
            int j = string.length() + this.firstCharacterIndex;
            if (cursor == this.firstCharacterIndex) {
                this.firstCharacterIndex = this.firstCharacterIndex - this.textRenderer.trimToWidth(this.text, i, true).length();
            }

            if (cursor > j) {
                this.firstCharacterIndex += cursor - j;
            } else if (cursor <= this.firstCharacterIndex) {
                this.firstCharacterIndex = this.firstCharacterIndex - (this.firstCharacterIndex - cursor);
            }

            this.firstCharacterIndex = MathHelper.clamp(this.firstCharacterIndex, 0, this.text.length());
        }
    }

    /**
     * Whether the widget is visible.
     *
     * @return true if the widget is visible.
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