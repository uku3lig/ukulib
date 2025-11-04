package net.uku3lig.ukulib.neoforge.utils;

import net.minecraft.client.KeyMapping;
import net.neoforged.fml.loading.FMLPaths;
import net.uku3lig.ukulib.utils.Ukutils;

import java.nio.file.Path;

public class UkutilsNeoForge extends Ukutils {
    @Override
    public Path getConfigPath(String name) {
        return FMLPaths.CONFIGDIR.get().resolve(name);
    }

    @Override
    protected void registerKeybindingInternal(KeyMapping keyBinding) {
        // we do nothing here, see UkulibNeoForge
    }
}
