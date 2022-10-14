package net.uku3lig.ukulib.api;

import net.minecraft.client.gui.screen.Screen;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;

import java.util.function.Function;

public interface UkulibAPI {
    Function<Screen, AbstractConfigScreen<?>> supplyConfigScreen();
}
