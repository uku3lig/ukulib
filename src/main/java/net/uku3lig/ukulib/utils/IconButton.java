package net.uku3lig.ukulib.utils;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * A simple button with an icon. Works best when the button is square.
 */
public class IconButton extends ButtonWidget {
    private final Identifier texture;
    private final int u;
    private final int v;
    private final int iconWidth;
    private final int iconHeight;
    private final int textureWidth;
    private final int textureHeight;

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
     * @param iconWidth     The width of the icon
     * @param iconHeight    The height of the icon
     * @param textureWidth  The width of the texture
     * @param textureHeight The height of the texture
     * @param pressAction   The action to perform when the button is pressed
     */
    public IconButton(int x, int y, int width, int height, Identifier texture, int u, int v, int iconWidth, int iconHeight, int textureWidth, int textureHeight, PressAction pressAction) {
        super(x, y, width, height, Text.empty(), pressAction, DEFAULT_NARRATION_SUPPLIER);
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    /**
     * Makes a new button with a centered icon. The rendered icon will be the whole texture.
     *
     * @param x             The x position of the button
     * @param y             The y position of the button
     * @param width         The width of the button
     * @param height        The height of the button
     * @param texture       The texture of the icon
     * @param textureWidth  The width of the texture
     * @param textureHeight The height of the texture
     * @param pressAction   The action to perform when the button is pressed
     */
    public IconButton(int x, int y, int width, int height, Identifier texture, int textureWidth, int textureHeight, PressAction pressAction) {
        this(x, y, width, height, texture, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight, pressAction);
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderButton(context, mouseX, mouseY, delta);

        // center the icon
        int rx = this.getX() + (this.width - this.iconWidth) / 2;
        int ry = this.getY() + (this.height - this.iconHeight) / 2;

        context.drawTexture(this.texture, rx, ry, this.u, this.v, this.iconWidth, this.iconHeight, this.textureWidth, this.textureHeight);
    }
}
