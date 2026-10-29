import com.modrinth.minotaur.ModrinthExtension
import org.gradle.kotlin.dsl.configure

plugins {
    id("multiloader-base")
}

val common = project(":common")
val commonMain = common.sourceSets["main"]
val commonCompileJava = common.tasks.getByName<JavaCompile>(commonMain.compileJavaTaskName)
val commonProcessResources =
    common.tasks.getByName<ProcessResources>(commonMain.processResourcesTaskName)

dependencies {
    implementation(project(":common"))
}

sourceSets.main {
    runtimeClasspath += commonMain.runtimeClasspath
    compileClasspath += commonMain.compileClasspath
}

tasks {
    processResources {
        from(commonProcessResources.destinationDir)
        dependsOn(commonProcessResources)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        inputs.property("version", version)
        inputs.property("minecraftVersion", BuildConfig.MINECRAFT_VERSION)
        inputs.property("featurecreepVersion", BuildConfig.FEATURECREEP_VERSION)

        filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml", "module.xml")) {
            expand(
                mapOf(
                    "version" to inputs.properties["version"],
                    "minecraftVersion" to inputs.properties["minecraftVersion"],
                    "featurecreepVersion" to inputs.properties["featurecreepVersion"]
                )
            )
        }
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.FAIL
        from(rootDir.resolve("LICENSE"))
        from(commonCompileJava.destinationDirectory)
    }
}

var releaseType = "release"
if (version.toString().contains("alpha")) releaseType = "alpha"
else if (version.toString().contains("beta")) releaseType = "beta"

if (project.name != "featurecreep") {
    apply(plugin = "com.modrinth.minotaur")
    configure<ModrinthExtension> {
        token = System.getenv("MODRINTH_TOKEN")
        projectId = BuildConfig.MODRINTH_PROJECT_ID
        versionNumber = "$version-${project.name}"
        versionType = releaseType
        uploadFile.set(tasks.jar)
        gameVersions.add(BuildConfig.MINECRAFT_VERSION)
        loaders.add(project.name)
        changelog = "See https://github.com/uku3lig/ukulib/releases/tag/${version}"
    }
}