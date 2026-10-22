package net.uku3lig.ukulibtest;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.uku3lig.ukulib.neoforge.UkulibNFProvider;

@Mod(value = "ukulib_test", dist = Dist.CLIENT)
public class TestNeoForge {
    public TestNeoForge(ModContainer container) {
        container.registerExtensionPoint(UkulibNFProvider.class, TestUkulibIntegration::new);
    }
}
