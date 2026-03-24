plugins {
    id("multiloader-base")
    id("com.modrinth.minotaur")
}

val common = project(":common")
val commonCompileJava = common.tasks.getByName<JavaCompile>(common.sourceSets["main"].compileJavaTaskName)
val commonProcessResources =
    common.tasks.getByName<ProcessResources>(common.sourceSets["main"].processResourcesTaskName)

dependencies {
    implementation(common)
}

tasks {
    processResources {
        from(commonProcessResources.destinationDir)
        dependsOn(commonProcessResources)

        inputs.property("version", version)

        filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
            expand(mapOf("version" to inputs.properties["version"]))
        }
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.FAIL
        from(rootDir.resolve("LICENSE"))
        from(commonCompileJava.destinationDirectory)
    }
}

var releaseType = "release"
if (version.toString().contains("alpha")) releaseType = "alpha"
else if (version.toString().contains("beta")) releaseType = "beta"

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    projectId = BuildConfig.MODRINTH_PROJECT_ID
    versionNumber = "$version-${project.name}"
    versionType = releaseType
    uploadFile.set(tasks.jar)
    gameVersions.add(BuildConfig.MINECRAFT_VERSION)
    loaders.add(project.name) // either fabric or neoforge, which are valid values
    changelog = "See https://github.com/uku3lig/ukulib/releases/tag/${version}"
}