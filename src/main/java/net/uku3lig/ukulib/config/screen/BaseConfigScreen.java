package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.utils.Ukutils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

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

        Collection<ClickableWidget> invalid = this.getInvalidOptions();
        doneButton.active = invalid.isEmpty();

        if (doneButton.active) {
            doneButton.setTooltip(Tooltip.of(Text.empty()));
        } else {
            String invalidNames = invalid.stream()
                    .map(ClickableWidget::getMessage)
                    .map(Text::getString)
                    .collect(Collectors.joining(", "));

            doneButton.setTooltip(Tooltip.of(Text.translatable("ukulib.option.invalid", invalidNames)));
        }
    }

    /**
     * Finds invalid options in the config screen. Called once every tick.
     *
     * @return The collection of invalid options.
     */
    protected Collection<ClickableWidget> getInvalidOptions() {
        return Collections.emptyList();
    }

    @Override
    public void removed() {
        if (getInvalidOptions().isEmpty()) {
            manager.saveConfig();
        }
    }
}
