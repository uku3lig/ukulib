plugins {
    id("multiloader-base")
    id("maven-publish")

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = rootProject.name + "-" + project.name
            version = version

            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "UkuReleases"
            url = uri("https://maven.uku3lig.net/releases")
            credentials {
                username = "uku"
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
        maven {
            name = "UkuSnapshots"
            url = uri("https://maven.uku3lig.net/snapshots")
            credentials {
                username = "uku"
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}

var releaseType = "release"
if (version.toString().contains("alpha")) releaseType = "alpha"
else if (version.toString().contains("beta")) releaseType = "beta"

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    projectId = "Y8uFrUil"
    versionNumber = version.toString()
    versionType = releaseType
    gameVersions.add(BuildConfig.MINECRAFT_VERSION) // Must be an array, even with only one version
    loaders.add(project.name) // either fabric or neoforge, which are valid values
    changelog = "See https://github.com/uku3lig/ukulib/releases/tag/${version}"
}