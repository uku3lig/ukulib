package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.uku3lig.ukulib.config.ConfigManager;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A color selection screen.
 */
public class ColorSelectScreen extends TextInputScreen<Integer> {
    /**
     * Constant for full alpha (255) with no color. <br>
     * Can be used like this: <code>color |= FULL_ALPHA</code>.
     */
    public static final int FULL_ALPHA = 0xFF000000;

    /**
     * Creates a color selection screen.
     * @param title The title of the screen
     * @param parent The parent screen
     * @param callback The action to be done after the screen is closed
     * @param last The last known color
     * @param manager The config manager
     */
    public ColorSelectScreen(Text title, Screen parent, Consumer<Integer> callback, int last, ConfigManager<?> manager) {
        super(parent, title, Text.of("Color"), callback, last, manager);
    }

    @Override
    protected void init() {
        super.init();
        addDrawableChild(ButtonWidget.builder(Text.of("Open Web Color Picker"), button -> Util.getOperatingSystem().open("https://colors-picker.com/hex-color-picker/"))
                .dimensions(this.width / 2 - 100, this.height - 51, 200, 20)
                .build());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        convert(getTextField().getText()).ifPresent(color -> renderColor(matrices, mouseX, mouseY, delta, color));
    }

    /**
     * Renders the selected color on the screen
     * @param matrices The matrix stack
     * @param mouseX The x position of the mouse
     * @param mouseY The y position of the mouse
     * @param delta The time delta
     * @param color The chosen color
     */
    @SuppressWarnings("unused")
    protected void renderColor(MatrixStack matrices, int mouseX, int mouseY, float delta, int color) {
        int x = this.width / 2 + 105;
        int y = 116;
        fill(matrices, x, y, x + 20, y + 20, color);
    }

    /**
     * Sets if alpha is allowed along with the RGB color.
     * @return <code>true</code> if allowed, <code>false</code> otherwise
     */
    protected boolean allowAlpha() {
        return true;
    }

    /**
     * The default alpha value for the color. Default disables alpha.
     * @return The default alpha value
     */
    protected byte defaultAlpha() {
        return (byte) 0xFF;
    }

    @Override
    public Optional<Integer> convert(String value) {
        try {
            value = value.replace("#", "");
            if (value.length() == 6 || (value.length() == 8 && allowAlpha())) {
                int color = Integer.parseUnsignedInt(value, 16);
                if (color <= 0xFFFFFF) color |= (defaultAlpha() << 24);

                return Optional.of(color);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public String format(Integer value) {
        if (!allowAlpha()) value &= 0x00FFFFFF;
        return "#" + Integer.toHexString(value);
    }
}
