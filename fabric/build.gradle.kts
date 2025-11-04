plugins {
    id("multiloader-platform")
    id("fabric-loom")
}

repositories {
    maven {
        url = uri("https://maven.terraformersmc.com/releases/")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${BuildConfig.MINECRAFT_VERSION}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${BuildConfig.FABRIC_LOADER_VERSION}")

    implementation("com.moandjiezana.toml:toml4j:${BuildConfig.TOML4J_VERSION}")
    include("com.moandjiezana.toml:toml4j:${BuildConfig.TOML4J_VERSION}")

    modCompileOnly("com.terraformersmc:modmenu:${BuildConfig.MODMENU_VERSION}")

    fun addEmbeddedFabricModule(name: String) {
        val module = fabricApi.module(name, BuildConfig.FABRIC_API_VERSION)
        modImplementation(module)
        include(module)
    }

    // v0 is still needed because it's the one that loads the resources from the jar file
    // TODO remove v0 when it's deprecated
    addEmbeddedFabricModule("fabric-resource-loader-v0")
    addEmbeddedFabricModule("fabric-resource-loader-v1")
    addEmbeddedFabricModule("fabric-command-api-v2")
    addEmbeddedFabricModule("fabric-key-binding-api-v1")
}

tasks.remapJar {
    destinationDirectory.set(file(rootProject.layout.buildDirectory).resolve("libs"))
}

modrinth {
    // With Loom, this MUST be set to `remapJar` instead of `jar`!
    uploadFile.set(tasks.remapJar)
    loaders.add("quilt")
}
