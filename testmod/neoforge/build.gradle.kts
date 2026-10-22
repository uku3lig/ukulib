plugins {
    id("testmod-platform")
    id("net.neoforged.moddev")
}

neoForge {
    version = BuildConfig.NEOFORGE_VERSION

    runs {
        create("client") {
            client()
            gameDirectory = rootProject.file("run")
        }
    }

    mods {
        create("ukulib-test") {
            sourceSet(sourceSets["main"])
            sourceSet(project(":testmod:common").sourceSets["main"])
        }
    }
}

dependencies {
    implementation(project(":neoforge"))
}