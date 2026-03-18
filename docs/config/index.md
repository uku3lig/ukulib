# Configuration framework

ukulib provides utilities for managing mod config files and providing vanilla-like config screens for a pleasing user experience.

## Creating a configuration class

ukulib lets you manage your config as a simple <abbr title="Plain Old Java Object">POJO</abbr> that implements [`Serializable`](https://docs.oracle.com/en/java/javase/25/docs//api/java.base/java/io/Serializable.html), nothing else needed! You can also freely add arrays, nested objects, etc. to your config class without trouble, the only general rule is that you should be able to represent your class as a JSON value.

```java title="ModConfig.java"
public class ModConfig implements Serializable {
    public boolean enabled = true;
    public double hurtCamIntensity = 0.3;
    // ...
}
```

## Registering the configuration

To take advantage of all the magical features of ukulib, you first need to register your configuration in a [`ConfigManager`](https://maven.uku3lig.net/javadoc/releases/net/uku3lig/ukulib-common/latest/.cache/unpack/net/uku3lig/ukulib/config/ConfigManager.html), which will wrap and keep your configuration object in memory, allowing for easy access, editing, and saving to disk.

```java title="MyMod.java"
public class MyMod {
    public static final ConfigManager<ModConfig> manager = ConfigManager.createDefault(ModConfig.class, "mymod");
}
```

A config manager only needs to be created once at any point during the game's execution; you can create it during the mod initialization phase of your loader, but that is not required. Creating a default config manager will instantiate your config class and prepare it to be serialized to `<.minecraft>/config/mymod.toml`.

If TOML doesn't suit you, you can instead call the constructor of `ConfigManager`, to which you will need to pass an instance of a [`ConfigSerializer`](https://maven.uku3lig.net/javadoc/releases/net/uku3lig/ukulib-common/latest/.cache/unpack/net/uku3lig/ukulib/config/serialization/package-summary.html). ukulib currently provides serialization to TOML and JSON, but you can freely make your own (de)serializer if those don't suit you.

## Accessing and using the configuration

Once you have a `ConfigManager`, the rest is pretty much trivial.

```java
if (manager.getConfig().enabled) {
    System.out.println("Mod is enabled!");
}

// ukulib doesn't do any hooking or anyting, you can just modify the object
// and changes will be automatically reflected on the rest of your mod!
manager.getConfig().hurtCamIntensity = 3;
// ConfigManager also exposes utility methods for saving, replacing and
// resetting the config, should you need that
manager.saveConfig();
// You can also freely use getters and setters (even Lombok!) without losing
// any functionality
manager.getConfig().setEnabled(false);
```
