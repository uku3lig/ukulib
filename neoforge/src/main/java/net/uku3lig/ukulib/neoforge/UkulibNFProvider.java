package net.uku3lig.ukulib.neoforge;

import net.neoforged.fml.IExtensionPoint;
import net.uku3lig.ukulib.api.UkulibAPI;

/**
 * Simple interface to wire {@link UkulibAPI} into NeoForge's {@link IExtensionPoint}
 */
@FunctionalInterface
public interface UkulibNFProvider extends IExtensionPoint {
    /**
     * Supplier for an instance of {@link UkulibAPI}
     * @return the instance
     */
    UkulibAPI get();
}
