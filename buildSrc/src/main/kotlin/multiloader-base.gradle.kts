plugins {
    id("java-library")
    id("idea")
}

group = "net.uku3lig"
version = BuildConfig.createVersionString(project)

base {
    archivesName = rootProject.name + "-" + project.name
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

repositories {
    mavenCentral()
}