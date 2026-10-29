plugins {
    id("multiloader-platform")
    id("net.minecraftforge.gradle")
}

minecraft {
    runs {
        configureEach {
            workingDir.set(rootProject.file("run"))
        }

        register("client")
    }
}

repositories {
    minecraft.mavenizer(this)
    maven { url = uri("https://maven.minecraftforge.net/") }
    maven { url = uri("https://libraries.minecraft.net/") }
}

val forgeToml4j = configurations.create("forgeToml4j") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    implementation(minecraft.dependency("net.minecraftforge:forge:${BuildConfig.FORGE_VERSION}"))
    implementation("com.moandjiezana.toml:toml4j:${BuildConfig.TOML4J_VERSION}")
    add(forgeToml4j.name, "com.moandjiezana.toml:toml4j:${BuildConfig.TOML4J_VERSION}") {
        // Forge supplies Gson; bundling toml4j's older copy would shadow it.
        exclude(group = "com.google.code.gson", module = "gson")
    }
}

tasks.jar {
    destinationDirectory.set(file(rootProject.layout.buildDirectory).resolve("libs"))
    manifest {
        attributes("MixinConfigs" to "ukulib-common.mixins.json")
    }
    from({ forgeToml4j.resolve().map(::zipTree) })
}

tasks.javadoc {
    exclude(
        "net/uku3lig/ukulib/forge/UkulibForge.java",
        "net/uku3lig/ukulib/forge/ForgePlatformUkutils.java"
    )
}
