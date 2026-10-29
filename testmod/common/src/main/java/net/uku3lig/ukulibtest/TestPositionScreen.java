package net.uku3lig.ukulibtest;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.uku3lig.ukulib.config.screen.PositionSelectScreen;
import net.uku3lig.ukulib.utils.UkuButton;

public class TestPositionScreen extends PositionSelectScreen {
    protected TestPositionScreen(Screen parent) {
        TestConfig config = TestConfig.manager.getConfig();
        super("Test Position", parent, config.x, config.y, TestConfig.manager, config::setPos);
    }

    @Override
    protected void draw(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta, int x, int y) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, UkuButton.DEFAULT_ICON, x, y, 0, 0, 16, 16, 16, 16);
    }
}
