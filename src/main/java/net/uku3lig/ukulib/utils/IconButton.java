package net.uku3lig.ukulib.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/**
 * A simple button with an icon. Works best when the button is square.
 */
public class IconButton extends TexturedButtonWidget {
    /**
     * Makes a new button with an icon.
     * @param x The x position of the button
     * @param y The y position of the button
     * @param width The width of the button
     * @param height The height of the button
     * @param u The x position of the icon in the texture
     * @param v The y position of the icon in the texture
     * @param hoveredVOffset The offset of the icon when hovered
     * @param texture The texture of the icon
     * @param textureWidth The width of the texture
     * @param textureHeight The height of the texture
     * @param pressAction The action to perform when the button is pressed
     */
    public IconButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, PressAction pressAction) {
        super(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // i love stealing minecraft's code
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        drawNineSlicedTexture(matrices, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getTextureY());

        super.renderButton(matrices, mouseX, mouseY, delta);
    }

    /**
     * PressableWidget#getTextureY()
     */
    private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isSelected()) {
            i = 2;
        }

        return 46 + i * 20;
    }
}
