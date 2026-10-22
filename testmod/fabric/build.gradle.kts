plugins {
    id("testmod-platform")
    id("net.fabricmc.fabric-loom")
}

loom {
    runs.named("client") {
        client()

        displayName = "fabric - Client"
        runDirectory = file("../../run")
        appendProjectPathToDisplayName = false
        generateRunConfig = true
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${BuildConfig.MINECRAFT_VERSION}")
    implementation("net.fabricmc:fabric-loader:${BuildConfig.FABRIC_LOADER_VERSION}")
    implementation(project(":fabric"))
}