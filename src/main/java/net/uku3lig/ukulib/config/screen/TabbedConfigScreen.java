package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.option.CheckedOption;
import org.jetbrains.annotations.NotNull;

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
    private TabNavigationBar tabWidget;
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

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
        this.tabWidget = TabNavigationBar.builder(this.tabManager, this.width)
                .addTabs(applyConfigChecked(this::getTabs, new Tab[0]))
                .build();
        this.addRenderableWidget(this.tabWidget);
        this.tabWidget.selectTab(0, false);

        LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footer.addChild(this.resetButton);
        footer.addChild(this.doneButton);

        this.layout.visitWidgets(widget -> {
            widget.setTabOrderGroup(1);
            this.addRenderableWidget(widget);
        });

        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        if (this.tabWidget != null) {
            this.tabWidget.updateWidth(this.width);
            int i = this.tabWidget.getRectangle().bottom();
            ScreenRectangle screenRect = new ScreenRectangle(0, i, this.width, this.height - 36 - i);
            this.tabManager.setTabArea(screenRect);
            this.layout.setHeaderHeight(i);
            this.layout.arrangeElements();
        }
    }

    @Override
    protected Collection<AbstractWidget> getInvalidOptions() {
        Set<AbstractWidget> invalid = new HashSet<>();

        for (Tab tab : this.tabWidget.getTabs()) {
            tab.visitChildren(c -> {
                if (c instanceof CheckedOption option && !option.isValid()) {
                    invalid.add(c);
                }
            });
        }

        return invalid;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        graphics.blit(RenderPipelines.GUI_TEXTURED, FOOTER_SEPARATOR, 0, Mth.roundToward(this.height - 36 - 2, 2), 0.0F, 0.0F, this.width, 2, 32, 2);
    }
}
