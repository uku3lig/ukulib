plugins {
    id("multiloader-base")
    id("java-library")

    id("net.fabricmc.fabric-loom")
    id("io.freefair.lombok") version "9.1.0"
}

dependencies {
    minecraft("com.mojang:minecraft:${BuildConfig.MINECRAFT_VERSION}")

    compileOnly("com.moandjiezana.toml:toml4j:${BuildConfig.TOML4J_VERSION}")

    // provided both by fabric and neoforge
    compileOnly("net.fabricmc:sponge-mixin:0.17.0+mixin.0.8.7")
    compileOnly("io.github.llamalad7:mixinextras-common:0.5.1")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.1")
}

fun exportSourceSetJava() {
    val configuration = configurations.create("commonJava") {
        isCanBeResolved = true
        isCanBeConsumed = true
    }

    val compileTask = tasks.getByName<JavaCompile>(sourceSets["main"].compileJavaTaskName)
    artifacts.add(configuration.name, compileTask.destinationDirectory) {
        builtBy(compileTask)
    }
}

fun exportSourceSetResources() {
    val configuration = configurations.create("commonResources") {
        isCanBeResolved = true
        isCanBeConsumed = true
    }

    val compileTask = tasks.getByName<ProcessResources>(sourceSets["main"].processResourcesTaskName)
    compileTask.apply {
        exclude("**/README.txt")
        exclude("/*.accesswidener")
    }

    artifacts.add(configuration.name, compileTask.destinationDir) {
        builtBy(compileTask)
    }
}

exportSourceSetJava()
exportSourceSetResources()

tasks.javadoc {
    exclude(
        "net/uku3lig/ukulib/config/impl/UkulibConfig.java",
        "net/uku3lig/ukulib/config/option/widget/TextInputWidget.java",
        "net/uku3lig/ukulib/mixin/*",
        "net/uku3lig/ukulib/utils/Services.java"
    )
}