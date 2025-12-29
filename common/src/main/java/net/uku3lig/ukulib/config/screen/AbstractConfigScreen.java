package net.uku3lig.ukulib.config.screen;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.option.CheckedOption;
import net.uku3lig.ukulib.config.option.WidgetCreator;
import net.uku3lig.ukulib.config.option.widget.WidgetCreatorList;

import java.io.Serializable;
import java.util.Collection;

/**
 * A screen used to edit a config.
 * Instances of this class should NOT be reused.
 *
 * @param <T> The type of the config
 */
@Slf4j
public abstract class AbstractConfigScreen<T extends Serializable> extends BaseConfigScreen<T> {
    /**
     * The widget used to display the options.
     *
     * @see AbstractConfigScreen#getWidgets(Serializable)
     */
    protected WidgetCreatorList buttonList;

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    /**
     * Creates a config screen.
     *
     * @param key     The translation key of the title
     * @param parent  The parent screen
     * @param manager The config manager
     */
    protected AbstractConfigScreen(String key, Screen parent, ConfigManager<T> manager) {
        super(key, parent, manager);
    }

    /**
     * The list of widgets that will be shown to the user when this screen is displayed.
     *
     * @param config The config
     * @return An array of {@link WidgetCreator}
     */
    protected abstract WidgetCreator[] getWidgets(T config);

    @Override
    protected void init() {
        super.init();

        this.layout.addTitleHeader(this.title, this.font);

        this.buttonList = this.layout.addToContents(new WidgetCreatorList(this.minecraft, this.width, this.layout));
        this.buttonList.addAll(super.applyConfigChecked(this::getWidgets, new WidgetCreator[0]));

        LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footer.addChild(this.resetButton);
        footer.addChild(this.doneButton);

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        this.buttonList.updateSize(this.width, this.layout);
    }

    @Override
    protected Collection<AbstractWidget> getInvalidOptions() {
        return buttonList.children().stream()
                .flatMap(e -> e.children().stream())
                .filter(AbstractWidget.class::isInstance)
                .map(e -> (AbstractWidget) e)
                .filter(w -> w instanceof CheckedOption option && !option.isValid())
                .toList();
    }
}
