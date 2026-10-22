package net.uku3lig.ukulibtest;

import net.uku3lig.ukulib.config.ConfigManager;

import java.io.Serializable;

public class TestConfig implements Serializable {
    public static final ConfigManager<TestConfig> manager = ConfigManager.createDefault(TestConfig.class, "ukulib-test");

    public boolean enable = true;
}
