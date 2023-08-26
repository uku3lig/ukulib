package net.uku3lig.ukulib.config.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.uku3lig.ukulib.config.ConfigManager;

import java.io.Serializable;

/**
 * The config class for ukulib
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UkulibConfig implements Serializable {
    @Getter
    private static final ConfigManager<UkulibConfig> manager = ConfigManager.createDefault(UkulibConfig.class, "ukulib");

    /**
     * The config instance
     *
     * @return The config
     */
    public static UkulibConfig get() {
        return manager.getConfig();
    }

    /**
     * Whether to show the button in the options menu
     */
    private boolean buttonInOptions = true;

    /**
     * Whether to enable the automatic mod menu integration
     */
    private boolean modMenuIntegration = true;
}
