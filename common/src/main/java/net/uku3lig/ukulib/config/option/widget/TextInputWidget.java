package net.uku3lig.ukulib.config.option.widget;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.*;
import net.uku3lig.ukulib.config.option.CheckedOption;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A widget to input text. Based on {@link net.minecraft.client.gui.components.EditBox}.
 */
// changes from vanilla: suggestion rendering & implements CheckedOption & renders invalid text
public class TextInputWidget extends AbstractWidget implements CheckedOption {
    private static final WidgetSprites TEXTURES = new WidgetSprites(
            Identifier.withDefaultNamespace("widget/text_field"), Identifier.withDefaultNamespace("widget/text_field_highlighted")
    );
    private static final String HORIZONTAL_CURSOR = "_";
    private static final int TEXT_COLOR = 0xFFE0E0E0;
    private static final int INVALID_COLOR = 0xFFFF0000;
    private final Font font;
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
    private long lastSwitchFocusTime = Util.getMillis();
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
        super(x, y, width, height, Component.literal(suggestion));
        this.font = Minecraft.getInstance().font;
        this.changedListener = changedListener;
        this.suggestion = suggestion;
        this.textPredicate = textPredicate;
        this.maxLength = maxLength;

        this.setText(initialValue);
        this.setTooltip(Tooltip.create(Component.literal(this.suggestion)));
        this.updateTextPosition();
    }

    @Override
    protected @NotNull MutableComponent createNarrationMessage() {
        Component text = this.getMessage();
        return Component.translatable("gui.narrate.editBox", text, this.text);
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
            String string = StringUtil.filterText(text);
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
        return Util.offsetByCodepoints(this.text, this.selectionStart, offset);
    }

    public void setCursor(int cursor, boolean select) {
        this.setSelectionStart(cursor);
        if (!select) {
            this.setSelectionEnd(this.selectionStart);
        }

        this.onChanged(this.text);
    }

    public void setSelectionStart(int cursor) {
        this.selectionStart = Mth.clamp(cursor, 0, this.text.length());
        this.updateFirstCharacterIndex(this.selectionStart);
    }

    public void setCursorToStart(boolean shiftKeyPressed) {
        this.setCursor(0, shiftKeyPressed);
    }

    public void setCursorToEnd(boolean shiftKeyPressed) {
        this.setCursor(this.text.length(), shiftKeyPressed);
    }

    @Override
    public boolean keyPressed(@NotNull KeyEvent input) {
        if (this.isActive() && this.isFocused()) {
            switch (input.key()) {
                case GLFW.GLFW_KEY_BACKSPACE -> {
                    this.erase(-1, input.hasControlDown());
                    return true;
                }
                case GLFW.GLFW_KEY_DELETE -> {
                    this.erase(1, input.hasControlDown());
                    return true;
                }
                case GLFW.GLFW_KEY_RIGHT -> {
                    if (input.hasControlDown()) {
                        this.setCursor(this.getWordSkipPosition(1), input.hasShiftDown());
                    } else {
                        this.moveCursor(1, input.hasShiftDown());
                    }

                    return true;
                }
                case GLFW.GLFW_KEY_LEFT -> {
                    if (input.hasControlDown()) {
                        this.setCursor(this.getWordSkipPosition(-1), input.hasShiftDown());
                    } else {
                        this.moveCursor(-1, input.hasShiftDown());
                    }

                    return true;
                }
                case GLFW.GLFW_KEY_HOME -> {
                    this.setCursorToStart(input.hasShiftDown());
                    return true;
                }
                case GLFW.GLFW_KEY_END -> {
                    this.setCursorToEnd(input.hasShiftDown());
                    return true;
                }
                default -> {
                    if (input.isSelectAll()) {
                        this.setCursorToEnd(false);
                        this.setSelectionEnd(0);
                        return true;
                    } else if (input.isCopy()) {
                        Minecraft.getInstance().keyboardHandler.setClipboard(this.getSelectedText());
                        return true;
                    } else if (input.isPaste()) {
                        this.write(Minecraft.getInstance().keyboardHandler.getClipboard());
                        return true;
                    } else {
                        if (input.isCut()) {
                            Minecraft.getInstance().keyboardHandler.setClipboard(this.getSelectedText());
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

    public boolean canConsumeInput() {
        return this.isActive() && this.isFocused();
    }

    @Override
    public boolean charTyped(@NotNull CharacterEvent input) {
        if (!this.canConsumeInput()) {
            return false;
        } else if (input.isAllowedChatCharacter()) {
            this.write(input.codepointAsString());
            return true;
        } else {
            return false;
        }
    }

    private int getClickPosition(MouseButtonEvent click) {
        int i = Math.min(Mth.floor(click.x()) - this.textX, this.getInnerWidth());
        String string = this.text.substring(this.firstCharacterIndex);
        return this.firstCharacterIndex + this.font.plainSubstrByWidth(string, i).length();
    }

    private void onDoubleClick(MouseButtonEvent click) {
        int i = this.getClickPosition(click);
        int j = this.getWordSkipPosition(-1, i);
        int k = this.getWordSkipPosition(1, i);
        this.setCursor(j, false);
        this.setCursor(k, true);
    }

    @Override
    public void onClick(@NotNull MouseButtonEvent click, boolean doubled) {
        if (doubled) {
            this.onDoubleClick(click);
        } else {
            this.setCursor(this.getClickPosition(click), click.hasShiftDown());
        }
    }

    @Override
    protected void onDrag(@NotNull MouseButtonEvent click, double d, double e) {
        this.setCursor(this.getClickPosition(click), true);
    }

    @Override
    public void playDownSound(@NotNull SoundManager soundManager) {
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float deltaTicks) {
        if (this.isVisible()) {
            if (this.drawsBackground()) {
                Identifier identifier = TEXTURES.get(this.isActive(), this.isFocused());
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier, this.getX(), this.getY(), this.getWidth(), this.getHeight());
            }

            int i = this.isValid() ? TEXT_COLOR : INVALID_COLOR;
            int j = this.selectionStart - this.firstCharacterIndex;
            String string = this.font.plainSubstrByWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
            boolean bl = j >= 0 && j <= string.length();
            boolean bl2 = this.isFocused() && (Util.getMillis() - this.lastSwitchFocusTime) / 300L % 2L == 0L && bl;
            int k = this.textX;
            int l = Mth.clamp(this.selectionEnd - this.firstCharacterIndex, 0, string.length());
            boolean textShadow = true;
            if (!string.isEmpty()) {
                String string2 = bl ? string.substring(0, j) : string;
                FormattedCharSequence orderedText = this.format(string2);
                graphics.drawString(this.font, orderedText, k, this.textY, i, textShadow);
                k += this.font.width(orderedText) + 1;
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
                graphics.drawString(this.font, this.format(string.substring(j)), k, this.textY, i, textShadow);
            }

            boolean canSuggestionBeRendered = this.font.width(string + suggestion) < this.getInnerWidth();
            if (!this.suggestion.isBlank() && canSuggestionBeRendered) {
                // render the suggestion (if possible)
                int x = this.getX() + this.getWidth() - 4 - this.font.width(suggestion);
                graphics.drawString(this.font, this.suggestion, x, this.textY, CommonColors.GRAY, textShadow);
            }

            if (l != j) {
                int n = this.textX + this.font.width(string.substring(0, l));
                graphics.textHighlight(Math.min(m, this.getX() + this.width), this.textY - 1, Math.min(n - 1, this.getX() + this.width), this.textY + 1 + 9, true);
            }

            if (bl2) {
                if (bl3) {
                    graphics.fill(m, this.textY - 1, m + 1, this.textY + 1 + 9, i);
                } else {
                    graphics.drawString(this.font, HORIZONTAL_CURSOR, m, this.textY, i, textShadow);
                }
            }

            if (this.isHovered()) {
                graphics.requestCursor(CursorTypes.IBEAM);
            }
        }
    }

    private FormattedCharSequence format(String string) {
        return FormattedCharSequence.forward(string, Style.EMPTY);
    }

    private void updateTextPosition() {
        if (this.font != null) {
            String string = this.font.plainSubstrByWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
            this.textX = this.getX() + (this.isCentered() ? (this.getWidth() - this.font.width(string)) / 2 : (this.drawsBackground ? 4 : 0));
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
            this.lastSwitchFocusTime = Util.getMillis();
        }
    }

    private boolean isCentered() {
        return false;
    }

    public int getInnerWidth() {
        return this.drawsBackground() ? this.width - 8 : this.width;
    }

    public void setSelectionEnd(int index) {
        this.selectionEnd = Mth.clamp(index, 0, this.text.length());
        this.updateFirstCharacterIndex(this.selectionEnd);
    }

    private void updateFirstCharacterIndex(int cursor) {
        if (this.font != null) {
            this.firstCharacterIndex = Math.min(this.firstCharacterIndex, this.text.length());
            int i = this.getInnerWidth();
            String string = this.font.plainSubstrByWidth(this.text.substring(this.firstCharacterIndex), i);
            int j = string.length() + this.firstCharacterIndex;
            if (cursor == this.firstCharacterIndex) {
                this.firstCharacterIndex = this.firstCharacterIndex - this.font.plainSubstrByWidth(this.text, i, true).length();
            }

            if (cursor > j) {
                this.firstCharacterIndex += cursor - j;
            } else if (cursor <= this.firstCharacterIndex) {
                this.firstCharacterIndex = this.firstCharacterIndex - (this.firstCharacterIndex - cursor);
            }

            this.firstCharacterIndex = Mth.clamp(this.firstCharacterIndex, 0, this.text.length());
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
    public void updateWidgetNarration(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE, this.createNarrationMessage());
    }

    @Override
    public boolean isValid() {
        return this.textPredicate.test(this.text);
    }
}