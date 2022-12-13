package net.uku3lig.ukulib.config.screen;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.options.Option;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.IConfig;
import net.uku3lig.ukulib.config.impl.BrokenConfigScreen;
import net.uku3lig.ukulib.utils.Ukutils;

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
        buttonList = new ButtonListWidget(this.minecraft, this.width, this.height, 32, this.height - 32, 25);

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
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.buttonList.render(mouseX, mouseY, delta);
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 20, 0xFFFFFF);
        super.render(mouseX, mouseY, delta);
    }

    @Override
    public void removed() {
        manager.saveConfig();
    }
}
