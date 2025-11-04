package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.option.WidgetCreator;

import java.io.Serializable;

/**
 * A tab made with {@link WidgetCreator} for ease of use.
 * @param <T> The type of the config
 */
public abstract class ButtonTab<T extends Serializable> extends GridLayoutTab {
    /**
     * The config manager
     */
    protected final ConfigManager<T> manager;

    /**
     * Creates a widget tab and adds all the {@link ButtonTab#getWidgets(Serializable) given widgets} to it
     * @param title The title of the tab
     * @param manager The config manager
     */
    protected ButtonTab(Component title, ConfigManager<T> manager) {
        super(title);
        this.manager = manager;

        GridLayout.RowHelper adder = this.layout.rowSpacing(4).createRowHelper(1);

        for (WidgetCreator option : getWidgets(manager.getConfig())) {
            adder.addChild(option.createWidget(0, 0, 210, 20));
        }
    }

    /**
     * Creates a widget tab and adds all the {@link ButtonTab#getWidgets(Serializable) given widgets} to it. The title is created from the given translation key.
     * @param key The translation key of the title
     * @param manager The config manager
     */
    protected ButtonTab(String key, ConfigManager<T> manager) {
        this(Component.translatable(key), manager);
    }

    /**
     * The widgets to be shown in this tab
     * @param config The config
     * @return The array of widgets to be displayed
     */
    protected abstract WidgetCreator[] getWidgets(T config);
}
