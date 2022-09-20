package net.uku3lig.ukulib.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class Ukutils {
    public static SimpleOption<Boolean> createButton(String key, Consumer<Screen> callback) {
        return createButton(key, Text.empty(), callback);
    }

    public static SimpleOption<Boolean> createButton(String key, Object value, Consumer<Screen> callback) {
        return createButton(key, Text.of(String.valueOf(value)), callback);
    }

    public static SimpleOption<Boolean> createButton(String key, Text text, Consumer<Screen> callback) {
        return new SimpleOption<>(key, SimpleOption.emptyTooltip(), (optionText, value) -> text,
                SimpleOption.BOOLEAN, true, v -> callback.accept(MinecraftClient.getInstance().currentScreen));
    }

    private Ukutils() {}
}
