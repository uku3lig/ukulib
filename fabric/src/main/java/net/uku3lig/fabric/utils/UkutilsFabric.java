package net.uku3lig.fabric.utils;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.uku3lig.ukulib.utils.Ukutils;

import java.nio.file.Path;

public class UkutilsFabric extends Ukutils {
    @Override
    public Path getConfigPath(String name) {
        return FabricLoader.getInstance().getConfigDir().resolve(name);
    }

    @Override
    public void registerKeybindingInternal(KeyMapping keyBinding) {
        KeyBindingHelper.registerKeyBinding(keyBinding);
    }
}