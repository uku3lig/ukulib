plugins {
    id("multiloader-platform")
    id("com.asbestosstar.fcdependencies")
}

repositories {
    mavenLocal()
}

val bundled = configurations.create("bundled") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

configurations.named("implementation") {
    extendsFrom(bundled)
}

dependencies {
    add(bundled.name, "com.moandjiezana.toml:toml4j:${BuildConfig.TOML4J_VERSION}") {
        // Minecraft supplies Gson; a bundled copy would shadow its newer version.
        exclude(group = "com.google.code.gson", module = "gson")
    }

    // FeatureCreep provides its API, loader, JBoss Modules and MixinExtras at runtime, so none of them are bundled.
    compileOnly("com.asbestosstar:featurecreepmc-26.3:${BuildConfig.FEATURECREEP_VERSION}")
}

fcdependencies {
    minecraftVersion.set(BuildConfig.MINECRAFT_VERSION)
    runMode.set("featurecreep")
    featureCreepLoaderVersion.set(BuildConfig.FEATURECREEP_VERSION)
    featureCreepBootstrapVersion.set(BuildConfig.FEATURECREEP_VERSION)
    featureCreepApiVersion.set(BuildConfig.FEATURECREEP_VERSION)
    featureCreepMinecraftApiVersion.set(BuildConfig.FEATURECREEP_VERSION)
}

tasks.jar {
    destinationDirectory.set(file(rootProject.layout.buildDirectory).resolve("libs"))
    from({ bundled.resolve().map(::zipTree) })
}
