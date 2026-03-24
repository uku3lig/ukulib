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

repositories {
    maven {
        name = "Maven for PR #2879" // https://github.com/neoforged/NeoForge/pull/2879
        url = uri("https://prmaven.neoforged.net/NeoForge/pr2879")
        content {
            includeModule("net.neoforged", "neoforge")
            includeModule("net.neoforged", "testframework")
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