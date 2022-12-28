package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.uku3lig.ukulib.config.ConfigManager;

import java.util.Optional;
import java.util.function.Consumer;

public class ColorSelectScreen extends TextInputScreen<Integer> {
    public static final int FULL_ALPHA = 0xFF000000;

    public ColorSelectScreen(Text title, Screen parent, Consumer<Integer> callback, int last, ConfigManager<?> manager) {
        super(parent, title, Text.of("Color"), callback, last, manager);
    }

    @Override
    protected void init() {
        super.init();
        addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 51, 200, 20, Text.of("Open Web Color Picker"), button ->
                MinecraftClient.getInstance().setScreen(new ConfirmLinkScreen(confirmed -> {
                    if (confirmed) Util.getOperatingSystem().open("https://colors-picker.com/hex-color-picker/");
                    MinecraftClient.getInstance().setScreen(this);
                }, "https://colors-picker.com/hex-color-picker/", true))));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        convert(textField.getText()).ifPresent(color -> renderColor(matrices, mouseX, mouseY, delta, color));
    }

    @SuppressWarnings("unused")
    protected void renderColor(MatrixStack matrices, int mouseX, int mouseY, float delta, int color) {
        int x = this.width / 2 + 105;
        int y = 116;
        fill(matrices, x, y, x + 20, y + 20, color);
    }

    @Override
    public Optional<Integer> convert(String value) {
        try {
            value = value.replace("#", "");
            if (value.length() != 8 && value.length() != 6) return Optional.empty();

            int color = Integer.parseUnsignedInt(value, 16);
            if (color <= 0xFFFFFF) color |= FULL_ALPHA;

            return Optional.of(color);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public String format(Integer value) {
        return "#" + Integer.toHexString(value);
    }
}
