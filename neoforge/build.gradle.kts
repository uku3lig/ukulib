plugins {
    id("multiloader-platform")
    id("net.neoforged.moddev")
}

neoForge {
    version = BuildConfig.NEOFORGE_VERSION

    runs {
        create("client") {
            client()
        }
    }

    mods {
        create(rootProject.name) {
            sourceSet(sourceSets["main"])
            sourceSet(project(":common").sourceSets["main"])
        }
    }
}

dependencies {
    implementation("com.moandjiezana.toml:toml4j:${BuildConfig.TOML4J_VERSION}")
    jarJar("com.moandjiezana.toml:toml4j:${BuildConfig.TOML4J_VERSION}")
}

tasks.jar {
    destinationDirectory.set(file(rootProject.layout.buildDirectory).resolve("libs"))
}

tasks.javadoc {
    exclude(
        "net/uku3lig/ukulib/neoforge/UkulibNeoForge.java",
        "net/uku3lig/ukulib/neoforge/NeoForgePlatformUkutils.java"
    )
}