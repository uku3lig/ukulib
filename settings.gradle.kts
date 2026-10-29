rootProject.name = "ukulib"

pluginManagement {
    repositories {
        mavenLocal()
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://maven.neoforged.net/releases/") }
        maven { url = uri("https://maven.minecraftforge.net/") }
        gradlePluginPortal()
    }
}

include("common", "fabric", "neoforge", "featurecreep")

// ForgeGradle eagerly prepares the Forge userdev toolchain during configuration.
// Keep it out of unrelated platform builds while still including it for the full build.
if (gradle.startParameter.taskNames.isEmpty() || gradle.startParameter.taskNames.any { task ->
        task == "build" || task == "check" || task == "modrinth" ||
                task.startsWith("publish") || task.contains(":forge")
    }) {
    include("forge")
}