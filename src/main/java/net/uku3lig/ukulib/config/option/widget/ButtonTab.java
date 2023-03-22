package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.option.ButtonCreator;

import java.io.Serializable;

public abstract class ButtonTab<T extends Serializable> extends GridScreenTab {
    protected final ConfigManager<T> manager;

    protected ButtonTab(Text title, ConfigManager<T> manager) {
        super(title);
        this.manager = manager;

        GridWidget.Adder adder = this.grid.setRowSpacing(4).createAdder(1);

        for (ButtonCreator option : getOptions(manager.getConfig())) {
            adder.add(option.createButton(0, 0, 210, 20));
        }
    }

    protected ButtonTab(String key, ConfigManager<T> manager) {
        this(Text.translatable(key), manager);
    }

    protected abstract ButtonCreator[] getOptions(T config);
}
