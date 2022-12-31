plugins {
    id("fabric-loom") version "1.0-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.8.0"
    id("io.freefair.lombok") version "6.6.1"
    id("org.ajoberstar.grgit") version "5.0.0-rc.3"

    `maven-publish`
    id("com.modrinth.minotaur") version "2.+"
}

val minecraftVersion: String by extra
val yarnMappings: String by extra
val loaderVersion: String by extra
val modVersion: String by extra
val mavenGroup: String by extra
val archivesBaseName: String by extra
val toml4jVersion: String by extra
val fabricVersion: String by extra


version = "$modVersion+$minecraftVersion${getVersionMetadata()}"
group = mavenGroup

if (System.getenv().containsKey("PUBLISH_RELEASE")) {
    tasks.publish.get().finalizedBy("modrinth")
}

loom {
    accessWidenerPath.set(file("src/main/resources/ukulib.accesswidener"))
}

repositories {
    maven {
        url = uri("https://maven.uku3lig.net/releases")
    }
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")

    include(implementation("com.moandjiezana.toml:toml4j:$toml4jVersion")!!)
    include(implementation("gs.mclo.java:mclogs-java:2.1.1")!!)

    include(modImplementation(fabricApi.module("fabric-resource-loader-v0", fabricVersion))!!)
    include(modImplementation(fabricApi.module("fabric-command-api-v1", fabricVersion))!!)
}

tasks.processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

val targetJavaVersion = 17
tasks.withType<JavaCompile>().configureEach {
    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    // If Javadoc is generated, this must be specified in that task too.
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}


java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }

    withSourcesJar()
    withJavadocJar()
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_$archivesBaseName" }
    }
}

// configure the maven publication
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

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("Y8uFrUil")
    versionNumber.set("${project.version}")
    versionType.set("release")
    uploadFile.set(tasks.findByName("remapJar")) // With Loom, this MUST be set to `remapJar` instead of `jar`!
    gameVersions.addAll(minecraftVersion) // Must be an array, even with only one version
    loaders.addAll("fabric", "quilt")
    changelog.set("See https://github.com/uku3lig/ukulib/releases/tag/${project.version}")
}

fun getVersionMetadata(): String {
    val env = System.getenv()

    if (env.containsKey("PUBLISH_RELEASE")) return ""

    val buildId = env["BUILD_NUMBER"]

    // CI builds only
    if (buildId != null) {
        return "-build.${buildId}"
    }

    if (grgit.head() != null) {
        val head = grgit.head()
        var id: String = head.abbreviatedId

        // Flag the build if the build tree is not clean
        if (!grgit.status().isClean) {
            id += "-dirty"
        }

        return "-rev.${id}"
    }

    // No tracking information could be found about the build
    return "-unknown"
}
