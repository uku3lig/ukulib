plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
    id("io.freefair.lombok") version "9.0.0"

    id("maven-publish")
    id("com.modrinth.minotaur") version "2.8.10"
}

version = "${project.property("mod_version")}+${project.property("minecraft_version")}${getVersionMetadata()}"
group = project.property("maven_group") as String

val archiveName = project.property("archives_base_name") as String

if (System.getenv().containsKey("PUBLISH_RELEASE")) {
    tasks.publish.get().finalizedBy("modrinth")
}

repositories {
    maven {
        url = uri("https://maven.uku3lig.net/releases")
    }
    maven {
        url = uri("https://maven.terraformersmc.com/releases/")
    }
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

    include(implementation("com.moandjiezana.toml:toml4j:${project.property("toml4j_version")}")!!)

    include(
        modImplementation(
            fabricApi.module(
                "fabric-resource-loader-v0",
                project.property("fabric_version") as String
            )
        )!!
    )
    include(
        modImplementation(
            fabricApi.module(
                "fabric-command-api-v2",
                project.property("fabric_version") as String
            )
        )!!
    )
    include(
        modImplementation(
            fabricApi.module(
                "fabric-key-binding-api-v1",
                project.property("fabric_version") as String
            )
        )!!
    )

    // optional deps
    modCompileOnly("com.terraformersmc:modmenu:${project.property("modmenu_version")}")
}

loom {
    accessWidenerPath = file("src/main/resources/ukulib.accesswidener")
}

base {
    archivesName = archiveName
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withSourcesJar()
    withJavadocJar()
}

tasks.processResources {
    inputs.property("version", version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${archiveName}" }
    }
}

tasks.javadoc {
    exclude(
        "net/uku3lig/ukulib/config/impl/UkulibConfig.java",
        "net/uku3lig/ukulib/config/option/widget/TextInputWidget.java"
    )
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components.getByName("java"))
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
    versionNumber = project.version.toString()
    versionType = releaseType
    uploadFile.set(tasks.remapJar) // With Loom, this MUST be set to `remapJar` instead of `jar`!
    gameVersions.add(project.property("minecraft_version") as String) // Must be an array, even with only one version
    loaders.addAll("fabric", "quilt")
    changelog = "See https://github.com/uku3lig/ukulib/releases/tag/${project.version}"
}

fun getVersionMetadata(): String {
    if (System.getenv().containsKey("PUBLISH_RELEASE")) return ""

    val buildId = System.getenv("GITHUB_RUN_NUMBER")

    // CI builds only
    return if (buildId != null) {
        "-build.${buildId}"
    } else {
        "-local"
    }
}
