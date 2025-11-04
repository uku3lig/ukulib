package net.uku3lig.ukulib;

import lombok.Getter;
import net.minecraft.client.gui.screens.Screen;
import net.uku3lig.ukulib.utils.ModMeta;
import net.uku3lig.ukulib.utils.Ukutils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class Ukulib {
    @Getter
    private static Ukutils utils;

    @Getter
    private final static Map<ModMeta, UnaryOperator<Screen>> configMods = new LinkedHashMap<>();

    public static void onPreLaunch(Ukutils utils) {
        Ukulib.utils = utils;
    }

    public static void onInitialize(Map<ModMeta, UnaryOperator<Screen>> configMods) {
        Ukulib.configMods.putAll(configMods);
    }
}
