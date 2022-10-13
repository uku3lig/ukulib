package net.uku3lig.ukulib.config.screen;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.gui.widgets.OptionButton;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.util.ScreenScaler;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.ConfigOption;
import net.uku3lig.ukulib.config.IConfig;

import java.util.Arrays;
import java.util.Optional;

/**
 * A screen used to edit a config.
 * Instances of this class should NOT be reused.
 *
 * @param <T> The type of the config
 */
@Log4j2
public abstract class AbstractConfigScreen<T extends IConfig<T>> extends Screen {
    private final Screen parent;
    /**
     * The config manager. Used to load and save the config.
     */
    protected final ConfigManager<T> manager;
    private final String title;

    /**
     * Creates a config screen.
     * @param parent The parent screen
     * @param title The title of the screen
     * @param manager The config manager
     */
    protected AbstractConfigScreen(Screen parent, String title, ConfigManager<T> manager) {
        this.parent = parent;
        this.title = title;
        this.manager = manager;
    }

    /**
     * The list of options that will be shown to the user when this screen is displayed.
     * @param config The config
     * @return An array of {@link ConfigOption}
     */
    protected abstract ConfigOption<?>[] getOptions(T config);

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        int i = 0;
        for(ConfigOption<?> option : getOptions(manager.getConfig())) {
            if (!option.isSlider()) {
                this.buttons.add(new OptionButton(option.getId(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), null, option.getTranslatedValue()));
            } else {
                this.buttons.add(new ConfigSlider(option.getId(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), option, option.getTranslatedValue(), Float.parseFloat(option.getGetter().get().toString())));
            }

            ++i;
        }

        this.buttons.add(new Button(200, this.width / 2 - 100, this.height / 6 + 168, TranslationStorage.getInstance().translate("gui.done")));
    }

    @Override
    protected void buttonClicked(Button arg) {
        if (Arrays.stream(getOptions(manager.getConfig())).filter(ConfigOption::isSlider).anyMatch(o -> o.getId() == arg.id)) return;
        
        if (arg.active) {
            if (arg.id < 100 && arg instanceof OptionButton) {
                Optional<ConfigOption<?>> option = Arrays.stream(getOptions(manager.getConfig())).filter(o -> o.getId() == arg.id).findFirst();
                if (option.isPresent()) {
                    option.get().getSetter().accept(1F);
                    arg.text = option.get().getTranslatedValue();
                }
            }

            if (arg.id == 200) {
                manager.saveConfig();
                this.minecraft.openScreen(this.parent);
            }

            ScreenScaler var2 = new ScreenScaler(this.minecraft.options, this.minecraft.actualWidth, this.minecraft.actualHeight);
            int var3 = var2.getScaledWidth();
            int var4 = var2.getScaledHeight();
            this.init(this.minecraft, var3, var4);
        }
    }

    @Override
    public void render(int i, int j, float f) {
        this.renderBackground();
        this.drawTextWithShadowCentred(this.textManager, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(i, j, f);
    }
}
