package net.uku3lig.ukulib.config.screen;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
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
        buttonList = new WidgetCreatorList(this.client, this.width, this.height - 64, 32, 25);
        buttonList.addAll(applyConfigChecked(this::getWidgets, new WidgetCreator[0]));

        this.addSelectableChild(buttonList);
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

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
        buttonList.render(drawContext, mouseX, mouseY, delta);
        drawContext.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    }
}
