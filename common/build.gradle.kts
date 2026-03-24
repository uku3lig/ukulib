plugins {
    id("multiloader-base")
    id("java-library")

    id("net.fabricmc.fabric-loom")
    id("io.freefair.lombok") version "9.2.0"
}

dependencies {
    minecraft("com.mojang:minecraft:${BuildConfig.MINECRAFT_VERSION}")

    compileOnly("com.moandjiezana.toml:toml4j:${BuildConfig.TOML4J_VERSION}")

    // provided both by fabric and neoforge
    compileOnly("net.fabricmc:sponge-mixin:0.17.0+mixin.0.8.7")
    compileOnly("io.github.llamalad7:mixinextras-common:0.5.1")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.1")
}

tasks.javadoc {
    exclude(
        "net/uku3lig/ukulib/config/impl/UkulibConfig.java",
        "net/uku3lig/ukulib/config/option/widget/TextInputWidget.java",
        "net/uku3lig/ukulib/mixin/*",
        "net/uku3lig/ukulib/utils/Services.java"
    )
}