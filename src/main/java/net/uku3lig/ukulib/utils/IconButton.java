package net.uku3lig.ukulib.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
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
    public void renderButton(int mouseX, int mouseY, float delta) {
        // i love stealing minecraft's code
        MinecraftClient.getInstance().getTextureManager().bindTexture(WIDGETS_LOCATION);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableDepthTest();
        blit(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        blit(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);

        super.renderButton(mouseX, mouseY, delta);
    }
}
