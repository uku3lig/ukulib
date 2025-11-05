package net.uku3lig.ukulib.neoforge;

import net.neoforged.fml.IExtensionPoint;
import net.uku3lig.ukulib.api.UkulibAPI;

@FunctionalInterface
public interface UkulibNFProvider extends IExtensionPoint {
    UkulibAPI get();
}
