package net.uku3lig.ukulib.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
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
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        DrawableHelper.drawTexture(matrices, this.getX(), this.getY(), 0, 46, this.width / 2, this.height);
        DrawableHelper.drawTexture(matrices, this.getX() + this.width / 2, this.getY(), 200 - this.width / 2, 46, this.width / 2, this.height);

        super.renderButton(matrices, mouseX, mouseY, delta);
    }
}
