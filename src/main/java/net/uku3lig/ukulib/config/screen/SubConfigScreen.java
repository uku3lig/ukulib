package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widgets.Button;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.IConfig;
import net.uku3lig.ukulib.config.ISubConfig;
import net.uku3lig.ukulib.config.serialization.SubConfigSerializer;

import java.util.function.Function;

/**
 * A screen used to edit a sub-config.
 * Instances of this class should NOT be reused.
 *
 * @param <T> The type of the config
 * @param <P> The type of the parent config
 * @see SubConfigSerializer
 */
@SuppressWarnings("unused")
public abstract class SubConfigScreen<T extends ISubConfig<T, P>, P extends IConfig<P>> extends AbstractConfigScreen<T> {
    private final ConfigManager<P> parentManager;

    /**
     * Creates a sub-config screen
     * @param parent The parent screen
     * @param title The title of the screen
     * @param manager The manager of the <b>parent</b> config
     * @param getter Gets the sub-config from the parent config
     */
    protected SubConfigScreen(Screen parent, String title, ConfigManager<P> manager, Function<P, T> getter) {
        super(parent, title, new ConfigManager<>(new SubConfigSerializer<>(manager, getter)));
        this.parentManager = manager;
    }

    @Override
    protected void buttonClicked(Button arg) {
        super.buttonClicked(arg);
        parentManager.saveConfig();
    }
}
