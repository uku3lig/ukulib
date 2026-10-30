package net.uku3lig.ukulib.config.compat;

import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

/**
 * Compatibility shim to account for the removal of titles from {@link net.minecraft.client.gui.components.tabs.Tab}. Don't use this in normal code.
 */
@Deprecated
public interface NamedTab {
    /**
     * The tab's name
     *
     * @return The tab's name
     */
    @Nullable
    Component getName();
}
