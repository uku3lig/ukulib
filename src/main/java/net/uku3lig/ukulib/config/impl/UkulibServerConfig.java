package net.uku3lig.ukulib.config.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.uku3lig.ukulib.config.ConfigManager;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Environment(EnvType.SERVER)
public class UkulibServerConfig implements Serializable {
    @Getter
    private static final ConfigManager<UkulibServerConfig> manager = ConfigManager.createDefault(UkulibServerConfig.class, "ukulib-server");

    private String versionRange = ">=1.0.0";
}
