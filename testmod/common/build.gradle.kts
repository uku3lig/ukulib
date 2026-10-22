plugins {
    id("testmod-base")
    id("net.fabricmc.fabric-loom")
    id("io.freefair.lombok") version "9.8.0"
}

dependencies {
    minecraft("com.mojang:minecraft:${BuildConfig.MINECRAFT_VERSION}")
    compileOnly(project(":common"))
}