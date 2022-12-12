package net.uku3lig.ukulib.config.screen;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.IConfig;
import net.uku3lig.ukulib.config.impl.BrokenConfigScreen;
import net.uku3lig.ukulib.utils.Ukutils;

import java.util.List;

/**
 * A screen used to edit a config.
 * Instances of this class should NOT be reused.
 *
 * @param <T> The type of the config
 */
@Log4j2
public abstract class AbstractConfigScreen<T extends IConfig<T>> extends GameOptionsScreen {
    /**
     * The config manager. Used to load and save the config.
     */
    protected final ConfigManager<T> manager;

    /**
     * The widget used to display the options.
     * @see AbstractConfigScreen#getOptions(IConfig)
     */
    protected ButtonListWidget buttonList;

    /**
     * Creates a config screen.
     * @param parent The parent screen
     * @param title The title of the screen
     * @param manager The config manager
     */
    protected AbstractConfigScreen(Screen parent, Text title, ConfigManager<T> manager) {
        super(parent, MinecraftClient.getInstance().options, title);
        this.manager = manager;
    }

    /**
     * The list of options that will be shown to the user when this screen is displayed.
     * @param config The config
     * @return An array of {@link Option}
     */
    protected abstract Option[] getOptions(T config);

    @Override
    protected void init() {
        super.init();
        buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);

        try {
            buttonList.addAll(getOptions(manager.getConfig()));
        } catch (Exception e) {
            log.error("Error while getting options, replacing config with the default one", e);
            manager.replaceConfig(manager.getConfig().defaultConfig());
            try {
                buttonList.addAll(getOptions(manager.getConfig()));
            } catch (Exception e2) {
                log.error("Error while getting options with the default config, this is a bug", e2);
                MinecraftClient.getInstance().openScreen(new BrokenConfigScreen(parent));
            }
        }

        this.children.add(buttonList);
        drawFooterButtons();
    }

    /**
     * Draws the buttons in the footer.
     */
    protected void drawFooterButtons() {
        this.addButton(Ukutils.doneButton(this.width, this.height, this.parent));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.buttonList.render(matrices, mouseX, mouseY, delta);
        DrawableHelper.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
        List<OrderedText> list = GameOptionsScreen.getHoveredButtonTooltip(this.buttonList, mouseX, mouseY);
        if (list != null) this.renderOrderedTooltip(matrices, list, mouseX, mouseY);
    }

    @Override
    public void removed() {
        manager.saveConfig();
    }
}
