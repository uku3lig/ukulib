package net.uku3lig.ukulib.config.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.uku3lig.ukulib.Ukulib;
import net.uku3lig.ukulib.config.ConfigManager;

import java.io.Serializable;

/**
 * The config class for ukulib
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Environment(EnvType.CLIENT)
public class UkulibClientConfig implements Serializable {
    @Getter
    private static final ConfigManager<UkulibClientConfig> manager = ConfigManager.createDefault(UkulibClientConfig.class, Ukulib.MOD_ID);

    /**
     * The config instance
     *
     * @return The config
     */
    public static UkulibClientConfig get() {
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

    /**
     * The UUID of the head used in the options screen button
     */
    private String headName = "uku3lig";
}
