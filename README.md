![banner](https://raw.githubusercontent.com/uku3lig/ukulib/1.19/banner.png)

[![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.uku3lig.net%2Freleases%2Fnet%2Fuku3lig%2Fukulib%2Fmaven-metadata.xml&color=brightgreen&style=for-the-badge)](https://maven.uku3lig.net/#/releases/net/uku3lig/ukulib)
[![Javadoc badge](https://img.shields.io/badge/javadoc-latest-blue?style=for-the-badge)](https://maven.uku3lig.net/javadoc/releases/net/uku3lig/ukulib/latest)

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