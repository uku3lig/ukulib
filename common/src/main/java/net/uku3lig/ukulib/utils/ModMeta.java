package net.uku3lig.ukulib.utils;

import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Optional;

/**
 * Platform-independent data class to store basic information about a mod.
 *
 * @param id          The unique identifier of the mod
 * @param name        The fancy display name of the mod
 * @param description The description of the mod
 * @param icon        An optional supplier to a stream, containing the icon of the mod
 */
public record ModMeta(String id, String name, String description, Optional<IoSupplier<@NotNull InputStream>> icon) {
}