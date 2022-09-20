package net.uku3lig.ukulib.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class Ukutils {
    private static final Text EMPTY = Text.of("");

    public static Option createButton(String key, Consumer<Screen> callback) {
        return createButton(key, EMPTY, callback);
    }

    public static Option createButton(String key, Object value, Consumer<Screen> callback) {
        return createButton(key, Text.of(String.valueOf(value)), callback);
    }

    public static Option createButton(String key, Text text, Consumer<Screen> callback) {
        return CyclingOption.create(key, text, text, opt -> true, (opt, option, value) -> callback.accept(MinecraftClient.getInstance().currentScreen));
    }

    private Ukutils() {}
}
