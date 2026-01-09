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

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)

    withSourcesJar()
    withJavadocJar()
}

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
        val isReleaseBuild = project.hasProperty("build.release")
        val mavenUsername: String? by project // reads from ORG_GRADLE_PROJECT_mavenUsername
        val mavenPassword: String? by project // reads from ORG_GRADLE_PROJECT_mavenPassword

        maven {
            name = "Uku"
            url = uri("https://maven.uku3lig.net".let {
                if (isReleaseBuild) "$it/releases" else "$it/snapshots"
            })

            credentials {
                username = mavenUsername
                password = mavenPassword
            }
        }
    }
}