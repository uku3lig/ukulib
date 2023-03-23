package net.uku3lig.ukulib.config.option;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.function.UnaryOperator;

/**
 * A button that opens a screen when clicked.
 */
public class ScreenOpenButton implements ButtonCreator {
    private final String key;
    private final UnaryOperator<Screen> opener;

    /**
     * Creates a new button.
     * @param key The translation key of the text
     * @param opener The supplier for the new screen. The lambda parameter is the current screen when the button is pressed.
     */
    public ScreenOpenButton(String key, UnaryOperator<Screen> opener) {
        this.key = key;
        this.opener = opener;
    }

    @Override
    public ClickableWidget createButton(int x, int y, int width, int height) {
        return ButtonWidget.builder(Text.translatable(key), b -> openScreen())
                .dimensions(x, y, width, height)
                .build();
    }

    /**
     * Opens the new screen.
     */
    private void openScreen() {
        MinecraftClient.getInstance().setScreen(opener.apply(MinecraftClient.getInstance().currentScreen));
    }
}
