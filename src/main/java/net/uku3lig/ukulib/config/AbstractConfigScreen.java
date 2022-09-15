package net.uku3lig.ukulib.config;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

@Slf4j
public abstract class AbstractConfigScreen<T extends IConfig<T>> extends GameOptionsScreen {
    protected final ConfigManager<T> manager;
    protected ButtonListWidget buttonList;

    protected AbstractConfigScreen(Screen parent, Text title, ConfigManager<T> manager) {
        super(parent, MinecraftClient.getInstance().options, title);
        this.manager = manager;
    }

    protected abstract Option[] getOptions();

    @Override
    protected void init() {
        super.init();
        buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        buttonList.addAll(getOptions());
        this.addSelectableChild(buttonList);
        drawFooterButtons();
    }

    @SuppressWarnings("ConstantConditions")
    protected void drawFooterButtons() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.buttonList.render(matrices, mouseX, mouseY, delta);
        DrawableHelper.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
        List<OrderedText> list = GameOptionsScreen.getHoveredButtonTooltip(this.buttonList, mouseX, mouseY);
        this.renderOrderedTooltip(matrices, list, mouseX, mouseY);
    }

    @Override
    public void removed() {
        manager.saveConfig();
    }
}
