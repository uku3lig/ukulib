package net.uku3lig.ukulib;

import lombok.Getter;
import net.minecraft.client.gui.screens.Screen;
import net.uku3lig.ukulib.utils.ModMeta;
import net.uku3lig.ukulib.utils.Ukutils;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Ukulib {
    @Getter
    private static Ukutils utils;

    private static Supplier<Map<ModMeta, UnaryOperator<Screen>>> configModSupplier;

    public static void onPreLaunch(Ukutils utils) {
        Ukulib.utils = utils;
    }

    public static void onInitialize(Supplier<Map<ModMeta, UnaryOperator<Screen>>> modSupplier) {
        Ukulib.configModSupplier = modSupplier;
    }

    public static Map<ModMeta, UnaryOperator<Screen>> getConfigMods() {
        if (configModSupplier != null) {
            return configModSupplier.get();
        } else {
            return Collections.emptyMap();
        }
    }
}
