plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.modrinth.minotaur:com.modrinth.minotaur.gradle.plugin:2.8.10")
    implementation("org.ow2.asm:asm:9.9.1")
}