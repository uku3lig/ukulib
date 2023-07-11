package net.uku3lig.ukulib.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Simple accessor for {@link TabNavigationWidget}.
 */
@Mixin(TabNavigationWidget.class)
public interface TabNavigationWidgetAccessor {
    /**
     * The tabs stored by the widget.
     *
     * @return The tabs
     */
    @Accessor
    ImmutableList<Tab> getTabs();
}
