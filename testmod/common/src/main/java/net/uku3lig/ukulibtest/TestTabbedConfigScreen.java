package net.uku3lig.ukulibtest;

import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.StringUtil;
import net.uku3lig.ukulib.config.option.ColorOption;
import net.uku3lig.ukulib.config.option.CyclingOption;
import net.uku3lig.ukulib.config.option.InputOption;
import net.uku3lig.ukulib.config.option.WidgetCreator;
import net.uku3lig.ukulib.config.option.widget.ButtonTab;
import net.uku3lig.ukulib.config.screen.TabbedConfigScreen;
import net.uku3lig.ukulib.utils.Position;

public class TestTabbedConfigScreen extends TabbedConfigScreen<TestConfig> {
    public TestTabbedConfigScreen(Screen parent) {
        super("Test Tabbed Config", parent, TestConfig.manager);
    }

    @Override
    protected Tab[] getTabs(TestConfig config) {
        return new Tab[] { new SimpleTab(), new ComplexTab() };
    }

    public class SimpleTab extends ButtonTab<TestConfig> {
        protected SimpleTab() {
            super("Simple", TestTabbedConfigScreen.this.manager);
        }

        @Override
        protected WidgetCreator[] getWidgets(TestConfig config) {
            return new WidgetCreator[]{
                    CyclingOption.ofBoolean("Enabled", config.enable, v -> config.enable = v),
                    CyclingOption.ofTranslatableEnum("Position", Position.class, config.position, v -> config.position = v),
            };
        }
    }

    public class ComplexTab extends ButtonTab<TestConfig> {
        protected ComplexTab() {
            super("Complex", TestTabbedConfigScreen.this.manager);
        }

        @Override
        protected WidgetCreator[] getWidgets(TestConfig config) {
            return new WidgetCreator[]{
                    new ColorOption("Color Alpha", config.colorAlpha, v -> config.colorAlpha = v, true),
                    new InputOption("Alphanumeric Input", config.input, v -> config.input = v, StringUtil::isValidPlayerName),
            };
        }
    }
}
