import org.gradle.api.Project

object BuildConfig {
    const val MINECRAFT_VERSION: String = "26.1-snapshot-1"
    const val FABRIC_LOADER_VERSION: String = "0.18.4"
    const val FABRIC_API_VERSION: String = "0.140.2+26.1"
    // const val NEOFORGE_VERSION: String = ""

    const val TOML4J_VERSION: String = "0.7.2"
    const val MODMENU_VERSION: String = "18.0.0-alpha.3"

    const val MOD_VERSION: String = "2.0.0-alpha.1"

    fun createVersionString(project: Project): String {
        val builder = StringBuilder()

        val isReleaseBuild = project.hasProperty("build.release")
        val buildId = System.getenv("GITHUB_RUN_NUMBER")

        builder.append(MOD_VERSION).append("+mc").append(MINECRAFT_VERSION)

        if (!isReleaseBuild) {
            if (buildId != null) {
                builder.append("-build.${buildId}")
            } else {
                builder.append("-local")
            }
        }

        return builder.toString()
    }
}