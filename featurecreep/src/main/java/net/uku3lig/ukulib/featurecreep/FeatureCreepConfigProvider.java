package net.uku3lig.ukulib.featurecreep;

import featurecreep.loader.config.FCConfigEntry;
import featurecreep.loader.config.FCConfigProvider;
import net.minecraft.client.gui.screens.Screen;
import net.uku3lig.ukulib.config.impl.UkulibConfigScreen;


public final class FeatureCreepConfigProvider implements FCConfigProvider {
    @Override
    public FCConfigEntry createConfigEntry() {
        return FCConfigEntry.builder("ukulib")
                .displayName("ukulib")
                .screenFactory(parent -> new UkulibConfigScreen((Screen) parent))
                .build();//Allows for mod menu integration where you can choose your provider for config screens allowing ukulib to use itself as the config provider in the mod menu.
    }
}
