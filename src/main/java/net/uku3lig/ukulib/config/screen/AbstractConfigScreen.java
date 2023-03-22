package net.uku3lig.ukulib.config.screen;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.IConfig;
import net.uku3lig.ukulib.config.impl.BrokenConfigScreen;
import net.uku3lig.ukulib.config.option.ButtonCreator;
import net.uku3lig.ukulib.config.option.widget.ButtonCreatorList;

/**
 * A screen used to edit a config.
 * Instances of this class should NOT be reused.
 *
 * @param <T> The type of the config
 */
@Slf4j
public abstract class AbstractConfigScreen<T extends IConfig<T>> extends BaseConfigScreen<T> {
    /**
     * The widget used to display the options.
     * @see AbstractConfigScreen#getOptions(IConfig)
     */
    protected ButtonCreatorList buttonList;

    /**
     * Creates a config screen.
     * @param parent The parent screen
     * @param title The title of the screen
     * @param manager The config manager
     */
    protected AbstractConfigScreen(Screen parent, Text title, ConfigManager<T> manager) {
        super(title, parent, manager);
    }

    /**
     * The list of options that will be shown to the user when this screen is displayed.
     * @param config The config
     * @return An array of {@link SimpleOption}
     */
    protected abstract ButtonCreator[] getOptions(T config);

    @Override
    protected void init() {
        super.init();
        buttonList = new ButtonCreatorList(this.client, this.width, this.height, 32, this.height - 32, 25);

        try {
            buttonList.addAll(getOptions(manager.getConfig()));
        } catch (Exception e) {
            log.error("Error while getting options, replacing config with the default one", e);
            manager.replaceConfig(manager.getConfig().defaultConfig());
            try {
                buttonList.addAll(getOptions(manager.getConfig()));
            } catch (Exception e2) {
                log.error("Error while getting options with the default config, this is a bug", e2);
                MinecraftClient.getInstance().setScreen(new BrokenConfigScreen(parent));
            }
        }

        this.addSelectableChild(buttonList);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);
        buttonList.render(drawContext, mouseX, mouseY, delta);
        drawContext.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(drawContext, mouseX, mouseY, delta);
    }
}
