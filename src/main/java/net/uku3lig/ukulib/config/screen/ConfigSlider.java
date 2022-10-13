package net.uku3lig.ukulib.config.screen;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widgets.Button;
import net.uku3lig.ukulib.config.ConfigOption;
import org.lwjgl.opengl.GL11;

@Getter
@Setter
class ConfigSlider extends Button {
    private float value;
    private boolean dragged = false;
    private final ConfigOption<?> option;

    public ConfigSlider(int i, int j, int k, ConfigOption<?> arg, String string, float f) {
        super(i, j, k, 150, 20, string);
        this.option = arg;
        this.value = f;
    }

    @Override
    protected int getYImage(boolean flag) {
        return 0;
    }

    @Override
    protected void postRender(Minecraft minecraft, int i, int j) {
        if (this.visible) {
            if (this.dragged) {
                saveValue(i);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.blit(this.x + (int)(this.value * (this.width - 8)), this.y, 0, 66, 4, 20);
            this.blit(this.x + (int)(this.value * (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean isMouseOver(Minecraft minecraft, int i, int j) {
        if (super.isMouseOver(minecraft, i, j)) {
            saveValue(i);
            this.dragged = true;
            return true;
        } else {
            return false;
        }
    }

    private void saveValue(int i) {
        this.value = (float)(i - (this.x + 4)) / (float)(this.width - 8);
        if (this.value < 0.0F) {
            this.value = 0.0F;
        }

        if (this.value > 1.0F) {
            this.value = 1.0F;
        }

        option.getSetter().accept(value);
        this.text = option.getTranslatedValue();
    }

    @Override
    public void mouseReleased(int i, int j) {
        this.dragged = false;
    }
}
