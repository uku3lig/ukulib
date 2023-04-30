package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.option.CheckedOption;
import net.uku3lig.ukulib.utils.Ukutils;

import java.io.Serializable;

/**
 * Simple abstract class which provides utilities for config screens
 *
 * @param <T> The type of the config class
 */
public abstract class BaseConfigScreen<T extends Serializable> extends CloseableScreen {
    /**
     * The config manager
     */
    protected final ConfigManager<T> manager;

    private ButtonWidget doneButton;

    /**
     * Constructs the screen
     *
     * @param key     The translation key of the title
     * @param parent  The parent screen
     * @param manager The config manager
     */
    protected BaseConfigScreen(String key, Screen parent, ConfigManager<T> manager) {
        super(key, parent);
        this.manager = manager;
    }

    @Override
    protected void init() {
        doneButton = this.addDrawableChild(Ukutils.doneButton(this.width, this.height, this.parent));
    }

    @Override
    public void tick() {
        super.tick();

        doneButton.active = this.children().stream()
                .filter(CheckedOption.class::isInstance)
                .map(CheckedOption.class::cast)
                .allMatch(CheckedOption::isValid);
    }

    @Override
    public void removed() {
        manager.saveConfig();
    }
}
