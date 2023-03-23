package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
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

    /**
     * Constructs the screen
     *
     * @param title   The title of the screen
     * @param parent  The parent screen
     * @param manager The config manager
     */
    protected BaseConfigScreen(Text title, Screen parent, ConfigManager<T> manager) {
        super(title, parent);
        this.manager = manager;
    }

    @Override
    protected void init() {
        this.addDrawableChild(Ukutils.doneButton(this.width, this.height, this.parent));
    }

    @Override
    public void removed() {
        manager.saveConfig();
    }
}
