import org.gradle.api.Project

object BuildConfig {
    const val MINECRAFT_VERSION: String = "26.1"
    const val FABRIC_LOADER_VERSION: String = "0.18.4"
    const val FABRIC_API_VERSION: String = "0.144.0+26.1"
    const val NEOFORGE_VERSION: String = "26.1.0.0-alpha.0+rc-3.20260324.133102"

    const val TOML4J_VERSION: String = "0.7.2"
    const val MODMENU_VERSION: String = "18.0.0-alpha.6"

    const val MOD_VERSION: String = "2.0.0-alpha.1"

    const val MODRINTH_PROJECT_ID: String = "Y8uFrUil"

    fun createVersionString(project: Project): String {
        val builder = StringBuilder()

        val isReleaseBuild = project.hasProperty("build.release")
        val buildId = System.getenv("GITHUB_RUN_NUMBER")

        builder.append(MOD_VERSION).append("+").append(MINECRAFT_VERSION)

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