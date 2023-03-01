package net.uku3lig.ukulib.config.screen;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.math.MatrixStack;
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
@Slf4j
public abstract class AbstractConfigScreen<T extends IConfig<T>> extends GameOptionsScreen {
    /**
     * The config manager. Used to load and save the config.
     */
    protected final ConfigManager<T> manager;

    /**
     * The widget used to display the options.
     * @see AbstractConfigScreen#getOptions(IConfig)
     */
    protected OptionListWidget optionList;

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
     * @return An array of {@link SimpleOption}
     */
    protected abstract SimpleOption<?>[] getOptions(T config);

    @Override
    protected void init() {
        super.init();
        optionList = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);

        try {
            optionList.addAll(getOptions(manager.getConfig()));
        } catch (Exception e) {
            log.error("Error while getting options, replacing config with the default one", e);
            manager.replaceConfig(manager.getConfig().defaultConfig());
            try {
                optionList.addAll(getOptions(manager.getConfig()));
            } catch (Exception e2) {
                log.error("Error while getting options with the default config, this is a bug", e2);
                MinecraftClient.getInstance().setScreen(new BrokenConfigScreen(parent));
            }
        }

        this.addSelectableChild(optionList);
        drawFooterButtons();
    }

    /**
     * Draws the buttons in the footer.
     */
    protected void drawFooterButtons() {
        this.addDrawableChild(Ukutils.doneButton(this.width, this.height, this.parent));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        render(matrices, optionList, mouseX, mouseY, delta);
    }

    @Override
    public void removed() {
        manager.saveConfig();
    }
}
