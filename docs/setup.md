---
hide: [navigation]
---

# Setup

ukulib can both be used in single and multi-loader environments without any trouble.

## Versioning

ukulib tries to comply with [Semantic Versioning](https://semver.org), and adds a suffix representing the targeted Minecraft version. (Ex: `v2.0.0+26.1` targets Minecraft 26.1). ukulib is guaranteed to work with the targeted version, but might also work on other versions.

The easiest way to find the matching ukulib version for your mod is to go to [the Modrinth page](https://modrinth.com/mod/ukulib/versions) and to filter for the Minecraft version you are using.

If your project defines its dependecy versions in a `gradle.properties` file, you can simply add a new line to it:

```properties title="gradle.properties"
ukulib_version=%MAVEN_VERSION%
```

You will also need to add [my Maven repository](https://maven.uku3lig.net) to your repositories:

```kotlin title="build.gradle.kts"
repositories {
    maven { url = uri("https://maven.uku3lig.net/releases") }
}
```

See below for [development versions](#development-versions).

## Single loader

### Fabric

Add ukulib to your dependencies:

```kotlin title="build.gradle.kts"
dependencies {
    // minecraft & fabric loader
    implementation("net.uku3lig:ukulib-fabric:${project.property("ukulib_version")}")
}
```

You will also need to add it as a dependency in your `fabric.mod.json`:

```json title="fabric.mod.json"
{
  "depends": {
    // other dependencies like minecraft, fabric loader, etc
    "ukulib": "^%MAVEN_SHORT%"
  }
}
```

### NeoForge

Add ukulib to your dependencies:

```kotlin title="build.gradle.kts"
dependencies {
    implementation("net.uku3lig:ukulib-neoforge:${project.property("ukulib_version")}")
}
```

You will also need to add it to your `neoforge.mods.toml`:

```toml title="neoforge.mods.toml"
[[dependencies.yourmodid]]
modId = "ukulib"
type = "required"
versionRange = "[%MAVEN_SHORT%,3)"
ordering = "NONE"
side = "CLIENT"
```

## Multi-loader

There are plenty of ways to make multi-loader Minecraft mods, but the steps are fairly straightforward:

- If you have a "common" source set that does not depend on any mod loader/toolchain, you can use the `ukulib-common` artifact (eg. `#!kotlin compileOnly("net.uku3lig:ukulib-common:$ukulibVersion")`), which does not contain any platform specific code. You will still likely need to depend on the platform-specific artifacts in your other submodules.
- If your code for different loaders is completely separated, you can just follow the steps for single loader setups.

You can look at some of my mods for concrete examples (eg. [armor-hud](https://github.com/uku3lig/armor-hud))

## Development versions

[![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/uku3lig/ukulib/build.yml?branch=main)](https://github.com/uku3lig/ukulib/actions)
[![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.uku3lig.net%2Fsnapshots%2Fnet%2Fuku3lig%2Fukulib-common%2Fmaven-metadata.xml&strategy=latestProperty)](https://maven.uku3lig.net/#/snapshots/net/uku3lig)

Dev versions are published on every commit and follow roughly the same versioning scheme, with the addition of a `-build.XXX` suffix. These versions are published to the [**snapshots** repository](https://maven.uku3lig.net/#/snapshots/net/uku3lig) of my maven repo, so make sure to change the url in your build.gradle if you want to use them.

Unlike for releases, there is no real good "version browser", so you'll just have to use the Reposilite web UI and CTRL+F your way to the version you need :-)
