package net.uku3lig.ukulib.config.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.uku3lig.ukulib.config.ConfigManager;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UkulibConfig implements Serializable {
    @Getter
    private static final ConfigManager<UkulibConfig> manager = ConfigManager.createDefault(UkulibConfig.class, "ukulib");

    private boolean buttonInOptions = true;
    private boolean modMenuIntegration = true;
}
