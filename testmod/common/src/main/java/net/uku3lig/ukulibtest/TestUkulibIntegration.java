package net.uku3lig.ukulibtest;

import net.minecraft.client.gui.screens.Screen;
import net.uku3lig.ukulib.api.UkulibAPI;

import java.util.function.UnaryOperator;

public class TestUkulibIntegration implements UkulibAPI {
    @Override
    public UnaryOperator<Screen> supplyConfigScreen() {
        return TestConfigScreen::new;
    }
}
