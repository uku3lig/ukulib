---
hide: [navigation]
---

# Setup

ukulib can both be used in single and multi-loader environments without any trouble.

## Versioning

ukulib tries to comply with [Semantic Versioning](https://semver.org), and adds a suffix representing the targeted Minecraft version. (Ex: `v2.0.0+26.1` targets Minecraft 26.1). ukulib is guaranteed to work with the targeted version, but might also work on other versions.

The easiest way to find the matching ukulib version for your mod is to go to [the Modrinth page](https://modrinth.com/mod/ukulib/versions) and to filter for the Minecraft version you are using.

If your project defines its dependecy versions in a `gradle.properties` file, you can simply add a new line to it:

```properties
ukulib_version=2.0.0+26.1
```

You will also need to add [my Maven repository](https://maven.uku3lig.net) to your repositories:

```kotlin
repositories {
    maven { url = uri("https://maven.uku3lig.net/releases") }
}
```

## Single loader

### Fabric

Add ukulib to your dependencies:

```kotlin
dependencies {
    // minecraft & fabric loader
    implementation("net.uku3lig:ukulib-fabric:${project.property("ukulib_version")}")
}
```

You will also need to add it as a dependency in your `fabric.mod.json`:

```jsonc
{
  "depends": {
    // other dependencies like minecraft, fabric loader, etc
    "ukulib": "^2.0.0",
  },
}
```

### NeoForge

Add ukulib to your dependencies:

```kotlin
dependencies {
    implementation("net.uku3lig:ukulib-neoforge:${project.property("ukulib_version")}")
}
```

You will also need to add it to your `neoforge.mods.toml`:

```toml
[[dependencies.yourmodid]]
modId = "ukulib"
type = "required"
versionRange = "[2.0.0,3)"
ordering = "NONE"
side = "CLIENT"
```

## Multi-loader

There are plenty of ways to make multi-loader Minecraft mods, but the steps are fairly straightforward:

- If you have a "common" source set that does not depend on any mod loader/toolchain, you can use the `ukulib-common` artifact (eg. `compileOnly("net.uku3lig:ukulib-common:$ukulibVersion")`), which does not contain any platform specific code. You will still likely need to depend on the platform-specific artifacts in your other submodules.
- If your code for different loaders is completely separated, you can just follow the steps for single loader setups.

You can look at some of my mods for concrete examples (eg. [armor-hud](https://github.com/uku3lig/armor-hud))
