package net.uku3lig.ukulib.config.impl;

import lombok.Getter;
import lombok.Setter;
import net.uku3lig.ukulib.config.ConfigManager;

import java.io.Serializable;

/**
 * The config class for ukulib
 */
@Getter
@Setter
public class UkulibConfig implements Serializable {
    /**
     * The config manager for this config
     *
     * @return The config manager
     */
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
     *
     * @param buttonInOptions the new value
     * @return the current value
     */
    private boolean buttonInOptions = true;

    /**
     * Whether to enable the automatic mod menu integration
     *
     * @param modMenuIntegration the new value
     * @return the current value
     */
    private boolean modMenuIntegration = true;

    /**
     * Constructs the config
     */
    public UkulibConfig() {
        // default config
    }

    /**
     * Constructs the config
     *
     * @param buttonInOptions    Whether to show the button in the options menu
     * @param modMenuIntegration Whether to enable the automatic mod menu integration
     */
    public UkulibConfig(boolean buttonInOptions, boolean modMenuIntegration) {
        this.buttonInOptions = buttonInOptions;
        this.modMenuIntegration = modMenuIntegration;
    }
}
