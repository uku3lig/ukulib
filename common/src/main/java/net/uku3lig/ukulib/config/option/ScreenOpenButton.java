package net.uku3lig.ukulib.config.option;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.UnaryOperator;

/**
 * A button that opens a screen when clicked.
 */
public class ScreenOpenButton implements WidgetCreator {
    private final String key;
    private final UnaryOperator<Screen> opener;

    /**
     * Creates a new button.
     *
     * @param key    The translation key of the text
     * @param opener The supplier for the new screen. The lambda parameter is the current screen when the button is pressed.
     */
    public ScreenOpenButton(String key, UnaryOperator<Screen> opener) {
        this.key = key;
        this.opener = opener;
    }

    @Override
    public AbstractWidget createWidget(int x, int y, int width, int height) {
        return Button.builder(Component.translatable(key), _ -> openScreen())
                .bounds(x, y, width, height)
                .build();
    }

    /**
     * Opens the new screen.
     */
    private void openScreen() {
        Minecraft.getInstance().setScreen(opener.apply(Minecraft.getInstance().screen));
    }
}
