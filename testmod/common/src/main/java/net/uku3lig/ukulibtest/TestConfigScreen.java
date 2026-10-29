package net.uku3lig.ukulibtest;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.uku3lig.ukulib.config.impl.UkulibConfigScreen;
import net.uku3lig.ukulib.config.option.*;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;
import net.uku3lig.ukulib.utils.Position;
import net.uku3lig.ukulib.utils.Ukutils;

import java.util.Optional;

public class TestConfigScreen extends AbstractConfigScreen<TestConfig> {
    public TestConfigScreen(Screen parent) {
        super("Test Config Screen", parent, TestConfig.manager);
    }

    @Override
    protected WidgetCreator[] getWidgets(TestConfig config) {
        return new WidgetCreator[]{
                new TextOption("Simple Text"),
                new TextOption("Lined Text", true),
                new TextOption("Wide Lined Text", true, 20).wide(),
                new ScreenOpenButton("Ukulib Config", UkulibConfigScreen::new),
                new SimpleButton("Reveal", _ -> Ukutils.sendToast(Component.literal("Scientists reveal"), Component.literal("the Hi"))),
                CyclingOption.ofBoolean("Enabled", config.enable, v -> config.enable = v),
                CyclingOption.ofTranslatableEnum("Position", Position.class, config.position, v -> config.position = v),
                new ColorOption("Color", config.color, v -> config.color = v),
                new ColorOption("Color Alpha", config.colorAlpha, v -> config.colorAlpha = v, true),
                new InputOption("Alphanumeric Input", config.input, v -> config.input = v, StringUtil::isValidPlayerName),
                new TypedInputOption<>("Typed Input", String.valueOf(config.doubleInput), v -> config.doubleInput = v, this::parseDouble),
                new IntSliderOption("Int Slider", config.intSlider, v -> config.intSlider = v, IntSliderOption.DEFAULT_INT_TO_TEXT, 0, 10, 1),
                new SliderOption("Double Slider", config.doubleSlider, v -> config.doubleSlider = v, SliderOption.DEFAULT_VALUE_TO_TEXT, 0, 10, 0.1),
                new ScreenOpenButton("Tabbed Config", TestTabbedConfigScreen::new),
                new ScreenOpenButton("Position Screen", TestPositionScreen::new),
        };
    }

    private Optional<Double> parseDouble(String text) {
        try {
            return Optional.of(Double.parseDouble(text));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
