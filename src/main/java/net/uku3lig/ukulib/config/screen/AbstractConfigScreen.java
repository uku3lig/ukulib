package net.uku3lig.ukulib.config.screen;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
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

    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

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

        this.layout.addHeader(this.title, this.textRenderer);

        this.buttonList = this.layout.addBody(new WidgetCreatorList(this.client, this.width, this.layout));
        this.buttonList.addAll(applyConfigChecked(this::getWidgets, new WidgetCreator[0]));

        DirectionalLayoutWidget footer = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        footer.add(this.resetButton);
        footer.add(this.doneButton);

        this.layout.forEachChild(this::addDrawableChild);
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
        this.buttonList.position(this.width, this.layout);
    }

    @Override
    protected Collection<ClickableWidget> getInvalidOptions() {
        return buttonList.children().stream()
                .flatMap(e -> e.children().stream())
                .filter(ClickableWidget.class::isInstance)
                .map(e -> (ClickableWidget) e)
                .filter(w -> w instanceof CheckedOption option && !option.isValid())
                .toList();
    }
}
