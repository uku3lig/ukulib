plugins {
    id("java-library")
    id("idea")
}

group = "net.uku3lig.ukulibtest"
version = "0.0.0"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(25)
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

tasks.jar {
    enabled = false
}

repositories {
    mavenCentral()
}