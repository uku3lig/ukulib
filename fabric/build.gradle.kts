plugins {
    id("multiloader-platform")
    id("net.fabricmc.fabric-loom")
}

repositories {
    maven {
        url = uri("https://maven.terraformersmc.com/releases/")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${BuildConfig.MINECRAFT_VERSION}")
    implementation("net.fabricmc:fabric-loader:${BuildConfig.FABRIC_LOADER_VERSION}")

    implementation("com.moandjiezana.toml:toml4j:${BuildConfig.TOML4J_VERSION}")
    include("com.moandjiezana.toml:toml4j:${BuildConfig.TOML4J_VERSION}")

    compileOnly("com.terraformersmc:modmenu:${BuildConfig.MODMENU_VERSION}")
    runtimeOnly("com.terraformersmc:modmenu:${BuildConfig.MODMENU_VERSION}")

    fun addEmbeddedFabricModule(name: String) {
        val module = fabricApi.module(name, BuildConfig.FABRIC_API_VERSION)
        implementation(module)
        include(module)
    }

    addEmbeddedFabricModule("fabric-resource-loader-v1")
    addEmbeddedFabricModule("fabric-command-api-v2")
    addEmbeddedFabricModule("fabric-key-mapping-api-v1")
}

tasks.jar {
    destinationDirectory.set(file(rootProject.layout.buildDirectory).resolve("libs"))
}

tasks.javadoc {
    exclude(
        "net/uku3lig/ukulib/fabric/UkulibFabric.java",
        "net/uku3lig/ukulib/fabric/ModMenuRegistrar.java",
        "net/uku3lig/ukulib/fabric/FabricPlatformUkutils.java"
    )
}

modrinth {
    loaders.add("quilt")
}