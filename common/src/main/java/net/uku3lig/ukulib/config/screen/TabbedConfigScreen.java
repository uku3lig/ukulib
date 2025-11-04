package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.option.CheckedOption;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A config screen that utilizes a tabbed layout instead of a simple list of buttons.
 *
 * @param <T> The type of the config
 * @see net.uku3lig.ukulib.config.option.widget.ButtonTab
 */
public abstract class TabbedConfigScreen<T extends Serializable> extends BaseConfigScreen<T> {
    private TabNavigationBar tabNavigationBar;
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);

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
    protected void init() {
        super.init();
        this.tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.width)
                .addTabs(applyConfigChecked(this::getTabs, new Tab[0]))
                .build();
        this.addRenderableWidget(this.tabNavigationBar);
        this.tabNavigationBar.selectTab(0, false);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        if (this.tabNavigationBar != null) {
            this.tabNavigationBar.setWidth(this.width);
            this.tabNavigationBar.arrangeElements();
            int i = this.tabNavigationBar.getRectangle().bottom();
            ScreenRectangle screenRect = new ScreenRectangle(0, i, this.width, this.height - 36 - i);
            this.tabManager.setTabArea(screenRect);
        }
    }

    @Override
    protected Collection<AbstractWidget> getInvalidOptions() {
        Set<AbstractWidget> invalid = new HashSet<>();

        for (Tab tab : this.tabNavigationBar.getTabs()) {
            tab.visitChildren(c -> {
                if (c instanceof CheckedOption option && !option.isValid()) {
                    invalid.add(c);
                }
            });
        }

        return invalid;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        graphics.blit(RenderPipelines.GUI_TEXTURED, FOOTER_SEPARATOR, 0, Mth.roundToward(this.height - 36 - 2, 2), 0.0F, 0.0F, this.width, 2, 32, 2);
    }
}
