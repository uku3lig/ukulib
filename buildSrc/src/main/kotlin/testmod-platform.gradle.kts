plugins {
    id("testmod-base")
}

val common = project(":testmod:common")
val commonMain = common.sourceSets["main"]
val commonCompileJava = common.tasks.getByName<JavaCompile>(commonMain.compileJavaTaskName)
val commonProcessResources =
    common.tasks.getByName<ProcessResources>(commonMain.processResourcesTaskName)

dependencies {
    implementation(project(":testmod:common"))
}

sourceSets.main {
    runtimeClasspath += commonMain.runtimeClasspath
    compileClasspath += commonMain.compileClasspath
}

tasks {
    processResources {
        from(commonProcessResources.destinationDir)
        dependsOn(commonProcessResources)
    }
}