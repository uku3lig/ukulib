plugins {
    id("net.fabricmc.fabric-loom") version "1.14-SNAPSHOT"
    id("io.freefair.lombok") version "9.1.0"

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
    implementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

    include(implementation("com.moandjiezana.toml:toml4j:${project.property("toml4j_version")}")!!)

    include(implementation(fabricApi.module("fabric-resource-loader-v1", project.property("fabric_version") as String))!!)
    include(implementation(fabricApi.module("fabric-command-api-v2", project.property("fabric_version") as String))!!)
    include(implementation(fabricApi.module("fabric-key-binding-api-v1", project.property("fabric_version") as String))!!)

    // optional deps
    // compileOnly("com.terraformersmc:modmenu:${project.property("modmenu_version")}")
}

base {
    archivesName = archiveName
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

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
        "net/uku3lig/ukulib/config/option/widget/TextInputWidget.java",
        "net/uku3lig/ukulib/mixin/*"
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
    uploadFile.set(tasks.jar)
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
