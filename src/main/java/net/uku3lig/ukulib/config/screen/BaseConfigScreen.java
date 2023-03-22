package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.IConfig;

public abstract class BaseConfigScreen<T extends IConfig<T>> extends CloseableScreen {
    protected final ConfigManager<T> manager;

    /**
     * Constructs a closeable screen
     *
     * @param title  The title of the screen
     * @param parent The parent screen
     */
    protected BaseConfigScreen(Text title, Screen parent, ConfigManager<T> manager) {
        super(title, parent);
        this.manager = manager;
    }

    @Override
    public void removed() {
        manager.saveConfig();
    }
}
