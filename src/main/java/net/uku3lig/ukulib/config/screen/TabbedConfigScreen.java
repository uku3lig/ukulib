package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.IConfig;

public abstract class TabbedConfigScreen<T extends IConfig<T>> extends AbstractConfigScreen<T> {
    private TabNavigationWidget tabWidget;
    private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);

    /**
     * Creates a tabbed config screen.
     *
     * @param parent  The parent screen
     * @param title   The title of the screen
     * @param manager The config manager
     */
    protected TabbedConfigScreen(Screen parent, Text title, ConfigManager<T> manager) {
        super(parent, title, manager);
    }

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
}
