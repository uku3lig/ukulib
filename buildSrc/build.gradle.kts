plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.modrinth.minotaur:com.modrinth.minotaur.gradle.plugin:2.8.10")
}