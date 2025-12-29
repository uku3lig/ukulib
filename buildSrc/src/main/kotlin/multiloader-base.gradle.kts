plugins {
    id("java-library")
    id("idea")
    id("maven-publish")
}

group = "net.uku3lig"
version = BuildConfig.createVersionString(project)

base {
    archivesName = rootProject.name + "-" + project.name
}

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(25)
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

repositories {
    mavenCentral()
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