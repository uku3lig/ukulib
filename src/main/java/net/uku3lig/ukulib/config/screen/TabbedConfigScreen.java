package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.util.math.MathHelper;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.option.CheckedOption;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static net.minecraft.client.gui.screen.world.CreateWorldScreen.FOOTER_SEPARATOR_TEXTURE;

/**
 * A config screen that utilizes a tabbed layout instead of a simple list of buttons.
 *
 * @param <T> The type of the config
 * @see net.uku3lig.ukulib.config.option.widget.ButtonTab
 */
public abstract class TabbedConfigScreen<T extends Serializable> extends BaseConfigScreen<T> {
    private TabNavigationWidget tabWidget;
    private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);

    /**
     * Creates a tabbed config screen.
     *
     * @param key     The translation key of the title
     * @param parent  The parent screen
     * @param manager The config manager
     */
    protected TabbedConfigScreen(String key, Screen parent, ConfigManager<T> manager) {
        super(key, parent, manager);
    }

    /**
     * The list of tabs to be displayed.
     *
     * @param config The config
     * @return An array of tabs
     * @see net.uku3lig.ukulib.config.option.widget.ButtonTab
     */
    protected abstract Tab[] getTabs(T config);

    @Override
    public void tick() {
        super.tick();
        this.tabManager.tick();
    }

    @Override
    protected void init() {
        super.init();
        this.tabWidget = TabNavigationWidget.builder(this.tabManager, this.width)
                .tabs(getTabs(manager.getConfig()))
                .build();
        this.addDrawableChild(this.tabWidget);
        this.tabWidget.selectTab(0, false);
        this.initTabNavigation();
    }

    @Override
    protected void initTabNavigation() {
        if (this.tabWidget != null) {
            this.tabWidget.setWidth(this.width);
            this.tabWidget.init();
            int i = this.tabWidget.getNavigationFocus().getBottom();
            ScreenRect screenRect = new ScreenRect(0, i, this.width, this.height - 36 - i);
            this.tabManager.setTabArea(screenRect);
        }
    }

    @Override
    protected Collection<ClickableWidget> getInvalidOptions() {
        Set<ClickableWidget> invalid = new HashSet<>();

        for (Tab tab : this.tabWidget.tabs) {
            tab.forEachChild(c -> {
                if (c instanceof CheckedOption option && !option.isValid()) {
                    invalid.add(c);
                }
            });
        }

        return invalid;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);
        drawContext.drawTexture(FOOTER_SEPARATOR_TEXTURE, 0, MathHelper.roundUpToMultiple(this.height - 36 - 2, 2), 0.0F, 0.0F, this.width, 2, 32, 2);
        super.render(drawContext, mouseX, mouseY, delta);
    }
}
