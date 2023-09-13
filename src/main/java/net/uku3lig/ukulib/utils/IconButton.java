package net.uku3lig.ukulib.utils;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * A simple button with an icon. Works best when the button is square.
 */
@Getter
@Setter
@SuppressWarnings("javadoc")
public class IconButton extends ButtonWidget {
    /**
     * The texture of the icon
     *
     * @param texture The new texture
     * @return The texture
     */
    private Identifier texture;

    /**
     * The x position of the icon in the texture
     *
     * @param u The new x position
     * @return The x position
     */
    private int u;

    /**
     * The y position of the icon in the texture
     *
     * @param v The new y position
     * @return The y position
     */
    private int v;

    /**
     * The width of the texture
     *
     * @param textureWidth The new width
     * @return The width
     */
    private int textureWidth;

    /**
     * The height of the texture
     *
     * @param textureHeight The new height
     * @return The height
     */
    private int textureHeight;

    /**
     * Makes a new button with a centered icon.
     *
     * @param x              The x position of the button
     * @param y              The y position of the button
     * @param width          The width of the button
     * @param height         The height of the button
     * @param u              The x position of the icon in the texture
     * @param v              The y position of the icon in the texture
     * @param hoveredVOffset The offset of the icon when hovered
     * @param texture        The texture of the icon
     * @param textureWidth   The width of the texture
     * @param textureHeight  The height of the texture
     * @param pressAction    The action to perform when the button is pressed
     * @deprecated kept for compatibility, use {@link #IconButton(int, int, int, int, int, int, Identifier, int, int, ButtonWidget.PressAction)}
     */
    @Deprecated(forRemoval = true, since = "0.7.1")
    @SuppressWarnings("unused")
    public IconButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, ButtonWidget.PressAction pressAction) {
        this(x, y, width, height, u, v, texture, textureWidth, textureHeight, pressAction);
    }

    /**
     * Makes a new button with a centered icon.
     *
     * @param x             The x position of the button
     * @param y             The y position of the button
     * @param width         The width of the button
     * @param height        The height of the button
     * @param u             The x position of the icon in the texture
     * @param v             The y position of the icon in the texture
     * @param texture       The texture of the icon
     * @param textureWidth  The width of the texture
     * @param textureHeight The height of the texture
     * @param pressAction   The action to perform when the button is pressed
     */
    public IconButton(int x, int y, int width, int height, int u, int v, Identifier texture, int textureWidth, int textureHeight, ButtonWidget.PressAction pressAction) {
        super(x, y, width, height, Text.empty(), pressAction, DEFAULT_NARRATION_SUPPLIER);
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderButton(context, mouseX, mouseY, delta);

        // center the icon
        int rx = this.getX() + (this.width - this.textureWidth) / 2;
        int ry = this.getY() + (this.height - this.textureHeight) / 2;

        context.drawTexture(this.texture, rx, ry, 0, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }
}
