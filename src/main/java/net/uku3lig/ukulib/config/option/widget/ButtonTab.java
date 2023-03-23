package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.option.ButtonCreator;

import java.io.Serializable;

/**
 * A tab made with {@link ButtonCreator} for ease of use.
 * @param <T> The type of the config
 */
public abstract class ButtonTab<T extends Serializable> extends GridScreenTab {
    /**
     * The config manager
     */
    protected final ConfigManager<T> manager;

    /**
     * Creates a widget tab and adds all the widgets to it
     * @param title The title of the tab
     * @param manager The config manager
     */
    protected ButtonTab(Text title, ConfigManager<T> manager) {
        super(title);
        this.manager = manager;

        GridWidget.Adder adder = this.grid.setRowSpacing(4).createAdder(1);

        for (ButtonCreator option : getOptions(manager.getConfig())) {
            adder.add(option.createButton(0, 0, 210, 20));
        }
    }

    /**
     * Creates a widget tab and adds all the widgets to it. The title is created from the given translation key.
     * @param key The translation key of the title
     * @param manager The config manager
     */
    protected ButtonTab(String key, ConfigManager<T> manager) {
        this(Text.translatable(key), manager);
    }

    /**
     * The widgets to be shown in this tab
     * @param config The config
     * @return The array of widgets to be displayed
     */
    protected abstract ButtonCreator[] getOptions(T config);
}
