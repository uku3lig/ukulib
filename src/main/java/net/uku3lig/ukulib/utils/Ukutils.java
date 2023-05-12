package net.uku3lig.ukulib.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.io.File;

/**
 * Simple class for various utilities.
 */
@SuppressWarnings("unused")
public class Ukutils {
    /**
     * Creates a done button.
     *
     * @param width  The width of the screen
     * @param height The height of the screen
     * @param parent The parent screen
     * @return The generated button
     */
    public static ButtonWidget doneButton(int width, int height, Screen parent) {
        return ButtonWidget.builder(ScreenTexts.DONE, button -> MinecraftClient.getInstance().setScreen(parent))
                .dimensions(width / 2 - 100, height - 27, 200, 20)
                .build();
    }

    /**
     * Makes text coordinates based on the position of an icon.
     *
     * @param text         The text to be drawn
     * @param screenWidth  The width of the screen
     * @param textRenderer The text renderer
     * @param x            The x coordinate of the icon
     * @param y            The y coordinate of the icon
     * @param width        The width of the icon
     * @param height       The height of the icon
     * @return The tuple of coordinates
     */
    public static Vector2ic getTextCoords(Text text, int screenWidth, TextRenderer textRenderer, int x, int y, int width, int height) {
        Vector2i vector = new Vector2i(
                x + (width / 2) - (textRenderer.getWidth(text) / 2), // center
                y + height + 2 - textRenderer.fontHeight // center
        );

        int rx = x - ((screenWidth - width) / 2);
        if (Math.abs(rx) >= 2) {
            vector.set(
                    rx < 0 ? x + width + 2 /* left */ : x - 2 - textRenderer.getWidth(text) /* right */,
                    y + (height / 2) - (textRenderer.fontHeight / 2) // left/right
            );
        }

        return vector;
    }

    /**
     * Makes text coordinates based on the position of a standard 16x16 icon.
     *
     * @param text         The text to be drawn
     * @param screenWidth  The width of the screen
     * @param textRenderer The text renderer
     * @param x            The x coordinate of the icon
     * @param y            The y coordinate of the icon
     * @return The tuple of coordinates
     * @see Ukutils#getTextCoords(Text, int, TextRenderer, int, int, int, int)
     */
    public static Vector2ic getTextCoords(Text text, int screenWidth, TextRenderer textRenderer, int x, int y) {
        return getTextCoords(text, screenWidth, textRenderer, x, y, 16, 16);
    }

    /**
     * Retrieves the string text from an ordered text.
     *
     * @param text The ordered text
     * @return The value of the text
     */
    public static String getText(OrderedText text) {
        StringBuilder builder = new StringBuilder();
        text.accept((index, style, codePoint) -> {
            builder.append(Character.toChars(codePoint));
            return true;
        });

        return builder.toString();
    }

    /**
     * Creates a config path for a file name.
     *
     * @param name The name of the file
     * @return The path to the file
     */
    public static File getConfigPath(String name) {
        return FabricLoader.getInstance().getConfigDir().resolve(name).toFile();
    }

    private Ukutils() {
    }
}
