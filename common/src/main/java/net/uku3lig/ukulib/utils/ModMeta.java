package net.uku3lig.ukulib.utils;

import net.minecraft.server.packs.resources.IoSupplier;

import java.io.InputStream;
import java.util.Optional;

public record ModMeta(String id, String name, String description, Optional<IoSupplier<InputStream>> icon) {
}
