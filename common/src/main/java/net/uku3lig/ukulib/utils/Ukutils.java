package net.uku3lig.ukulib.utils;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FormattedCharSequence;
import net.uku3lig.ukulib.mixin.TextureManagerAccessor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Simple class for various utilities.
 */
public abstract class Ukutils {
    private static final Component ON = Component.literal("ON").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN);
    private static final Component OFF = Component.literal("OFF").withStyle(ChatFormatting.BOLD, ChatFormatting.RED);

    /**
     * The map of keybindings and their respective actions.
     *
     * @return The map.
     */
    @Getter
    private final Map<KeyMapping, Consumer<Minecraft>> keybindings = new HashMap<>();

    /**
     * Creates a done button.
     *
     * @param width  The width of the screen
     * @param height The height of the screen
     * @param parent The parent screen
     * @return The generated button
     */
    public Button doneButton(int width, int height, Screen parent) {
        return Button.builder(CommonComponents.GUI_DONE, button -> Minecraft.getInstance().setScreen(parent))
                .bounds(width / 2 - 100, height - 27, 200, 20)
                .build();
    }

    /**
     * Makes text coordinates based on the position of an icon.
     *
     * @param text        The text to be drawn
     * @param screenWidth The width of the screen
     * @param font        The font
     * @param x           The x coordinate of the icon
     * @param y           The y coordinate of the icon
     * @param width       The width of the icon
     * @param height      The height of the icon
     * @return The tuple of coordinates
     */
    public Vector2ic getTextCoords(Component text, int screenWidth, Font font, int x, int y, int width, int height) {
        Vector2i vector = new Vector2i(
                x + (width / 2) - (font.width(text) / 2), // center
                y + height + 2 - font.lineHeight // center
        );

        int rx = x - ((screenWidth - width) / 2);
        if (Math.abs(rx) >= 2) {
            vector.set(
                    rx < 0 ? x + width + 2 /* left */ : x - 2 - font.width(text) /* right */,
                    y + (height / 2) - (font.lineHeight / 2) // left/right
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
     * @see Ukutils#getTextCoords(Component, int, Font, int, int, int, int)
     */
    public Vector2ic getTextCoords(Component text, int screenWidth, Font textRenderer, int x, int y) {
        return getTextCoords(text, screenWidth, textRenderer, x, y, 16, 16);
    }

    /**
     * Retrieves the string text from a {@link FormattedCharSequence}.
     *
     * @param text The FormattedCharSequence
     * @return The value of the text
     */
    @SuppressWarnings("unused")
    public String getText(FormattedCharSequence text) {
        StringBuilder builder = new StringBuilder();
        text.accept((index, style, codePoint) -> {
            builder.append(Character.toChars(codePoint));
            return true;
        });

        return builder.toString();
    }

    /**
     * Retrieves the styled text from a {@link FormattedCharSequence}.
     *
     * @param text The FormattedCharSequence
     * @return The styled text
     */
    @SuppressWarnings("unused")
    public MutableComponent getStyledText(FormattedCharSequence text) {
        MutableComponent builder = Component.empty();
        text.accept((index, style, codePoint) -> {
            builder.append(Component.literal(Character.toString(codePoint)).setStyle(style));
            return true;
        });

        return builder;
    }

    /**
     * Creates a config path for a file name.
     *
     * @param name The name of the file
     * @return The path to the file
     */
    public abstract Path getConfigPath(String name);

    /**
     * Displays a simple toast message.
     *
     * @param title The title of the message
     * @param body  The body of the message
     */
    public void sendToast(Component title, @Nullable Component body) {
        ToastManager toastManager = Minecraft.getInstance().getToastManager();
        SystemToast.addOrUpdate(toastManager, SystemToast.SystemToastId.NARRATOR_TOGGLE, title, body);
    }

    /**
     * Checks if a texture exists and is registered.
     *
     * @param texture The texture to check
     * @return Whether the texture exists
     */
    @Contract("null -> false")
    public boolean textureExists(ResourceLocation texture) {
        TextureManagerAccessor textureManager = (TextureManagerAccessor) Minecraft.getInstance().getTextureManager();
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        return texture != null && (textureManager.getByPath().containsKey(texture) || resourceManager.getResource(texture).isPresent());
    }

    /**
     * Gets the texture of a player's head.
     *
     * @param username The username of the player
     * @return The texture of the player's head
     */
    @Nullable
    public ResourceLocation getHeadTex(String username) {
        if (ResourceLocation.isValidPath(username)) {
            username = username.toLowerCase(Locale.ROOT);
            return ResourceLocation.fromNamespaceAndPath("ukulib", "head_" + username);
        } else {
            return null;
        }
    }

    /**
     * Register a keybinding.
     *
     * @param keyBinding The keybinding
     * @param action     The action to be performed when the key is pressed
     */
    public void registerKeybinding(KeyMapping keyBinding, Consumer<Minecraft> action) {
        registerKeybindingInternal(keyBinding);
        this.keybindings.put(keyBinding, action);
    }

    protected abstract void registerKeybindingInternal(KeyMapping keyBinding);

    /**
     * Register a keybinding for a boolean option that can be toggled.
     * Warning: for the consumer, make sure NOT to use method reference if you're using ukulib's config;
     * do something like {@code b -> manager.getConfig().setValue(b)} instead.
     *
     * @param keyBinding The keybinding
     * @param getter     The boolean value getter
     * @param setter     The boolean value setter
     * @param message    The message to be displayed to the player when the key is pressed.
     *                   The new state (ON or OFF) will be appended to this message.
     */
    public void registerToggleBind(KeyMapping keyBinding, BooleanSupplier getter, BooleanConsumer setter, Component message) {
        Consumer<Minecraft> action = client -> {
            boolean newValue = !getter.getAsBoolean();
            setter.accept(newValue);

            if (client.player != null) {
                client.player.displayClientMessage(message.copy().append(" ").append(newValue ? ON : OFF), true);
            }
        };

        this.registerKeybinding(keyBinding, action);
    }

    protected Ukutils() {
    }
}
