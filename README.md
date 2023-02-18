![banner](https://raw.githubusercontent.com/uku3lig/ukulib/1.19.4/banner.png)

[![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.uku3lig.net%2Freleases%2Fnet%2Fuku3lig%2Fukulib%2Fmaven-metadata.xml&color=brightgreen&style=for-the-badge)](https://maven.uku3lig.net/#/releases/net/uku3lig/ukulib)
[![Javadoc badge](https://img.shields.io/badge/javadoc-latest-blue?style=for-the-badge)](https://maven.uku3lig.net/javadoc/releases/net/uku3lig/ukulib/latest)
![Code Climate maintainability](https://img.shields.io/codeclimate/maintainability/uku3lig/ukulib?style=for-the-badge)
![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/uku3lig/ukulib?style=for-the-badge)

The coolest fabric library mod on the internet, which has 0 (zero) dependencies. <br>
Javadoc can be found [here](https://maven.uku3lig.net/javadoc/releases/net/uku3lig/ukulib/latest) for the latest version, or at `https://maven.uku3lig.net/javadoc/releases/net/uku3lig/ukulib/VERSION` for older versions.

## Features
* A simple yet powerful config system, which also provides utilities for config screens.
* Only this for now... but more will come soon™️ ! (ideas and feedback are appreciated :D)

## How to use

Add the following to your `gradle.properties` (see above shield or the [maven repo](https://maven.uku3lig.net/#/releases/net/uku3lig/ukulib) for version info):
```properties
# https://github.com/uku3lig/ukulib
ukulib_version=...
```

`build.gradle`:
```groovy
repositories {
    maven {
        url "https://maven.uku3lig.net/releases"
    }
}

dependencies {
    modApi "net.uku3lig:ukulib:${project.ukulib_version}"

    // you can also include it directly if you don't want your users to download it
    include "net.uku3lig:ukulib:${project.ukulib_version}"
}
```

<details>
    <summary><b>Development versions</b></summary>

<a href="https://github.com/uku3lig/ukulib/actions"><img alt="GitHub Workflow Status" src="https://img.shields.io/github/actions/workflow/status/uku3lig/ukulib/build.yml?branch=1.19.3"></a>
<a href="https://maven.uku3lig.net/#/snapshots/net/uku3lig/ukulib"><img alt="Maven metadata URL" src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.uku3lig.net%2Fsnapshots%2Fnet%2Fuku3lig%2Fukulib%2Fmaven-metadata.xml"></a>
    
Development version numbers end with `-build.<build number>`, e.g. `0.2.2+1.19.2-build.65`. <br>
They are not guaranteed to be stable or even work at all, but they are available if you want to test the latest changes. <br>
They are also not published to the maven repo, so you have to add the following to your `build.gradle`:
```groovy
repositories {
    maven {
        url "https://maven.uku3lig.net/snapshots"
    }
}
```
</details>

## Integrating your config screen

Ukulib provides an api class, `UkulibAPI`, which you can extend to integrate your mod's config screen with ukulib. Example:
```java
public class YourUkulibAPIClass implements UkulibAPI {
    @Override
    public Function<Screen, AbstractConfigScreen<?>> supplyConfigScreen() {
        return parent -> new YourConfigScreen(parent, ...);
    }
}
```

You then need to add the `ukulib` entrypoint in your `fabric.mod.json`:
```json
"entrypoints": {
    ...
    "ukulib": [
      "your.awesome.mod.YourUkulibAPIClass"
    ]
  }
```
