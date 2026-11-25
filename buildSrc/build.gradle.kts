plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.modrinth.minotaur:com.modrinth.minotaur.gradle.plugin:2.10.0")
}