package net.uku3lig.ukulib.utils;

import java.nio.file.Path;
import java.util.Optional;

public record ModMeta(String id, String name, String description, Optional<Path> iconPath) {
}
