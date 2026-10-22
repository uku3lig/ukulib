package net.uku3lig.ukulibtest;

import net.minecraft.client.gui.screens.Screen;
import net.uku3lig.ukulib.config.option.CyclingOption;
import net.uku3lig.ukulib.config.option.WidgetCreator;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;

public class TestConfigScreen extends AbstractConfigScreen<TestConfig> {
    public TestConfigScreen(Screen parent) {
        super("Test Config Screen", parent, TestConfig.manager);
    }

    @Override
    protected WidgetCreator[] getWidgets(TestConfig config) {
        return new WidgetCreator[]{
                CyclingOption.ofBoolean("Enabled", config.enable, v -> config.enable = v)
        };
    }
}
