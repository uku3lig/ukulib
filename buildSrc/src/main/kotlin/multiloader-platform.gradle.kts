plugins {
    id("multiloader-base")
    id("com.modrinth.minotaur")
}

val commonJava: Configuration = configurations.create("commonJava") {
    isCanBeResolved = true
}
val commonResources: Configuration = configurations.create("commonResources") {
    isCanBeResolved = true
}

dependencies {
    compileOnly(project(path = ":common"))

    commonJava(project(path = ":common", configuration = "commonJava"))
    commonResources(project(path = ":common", configuration = "commonResources"))
}

sourceSets.apply {
    main {
        compileClasspath += commonJava
        runtimeClasspath += commonJava
    }
}

tasks {
    processResources {
        from(commonResources)

        inputs.property("version", version)

        filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
            expand(mapOf("version" to inputs.properties["version"]))
        }
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.FAIL
        from(rootDir.resolve("LICENSE"))
        from(commonJava)
    }
}

var releaseType = "release"
if (version.toString().contains("alpha")) releaseType = "alpha"
else if (version.toString().contains("beta")) releaseType = "beta"

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    projectId = BuildConfig.MODRINTH_PROJECT_ID
    versionNumber = version.toString()
    versionType = releaseType
    uploadFile.set(tasks.jar)
    gameVersions.add(BuildConfig.MINECRAFT_VERSION)
    loaders.add(project.name) // either fabric or neoforge, which are valid values
    changelog = "See https://github.com/uku3lig/ukulib/releases/tag/${version}"
}