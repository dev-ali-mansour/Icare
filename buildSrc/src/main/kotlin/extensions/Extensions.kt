package extensions

import org.gradle.api.Project
import java.util.Properties

fun Project.getSecret(key: String): String {
    val localProperties =
        Properties().apply {
            val propertiesFile = rootProject.file("local.properties")
            if (propertiesFile.exists()) {
                propertiesFile.inputStream().use { load(it) }
            }
        }
    return localProperties.getProperty(key) ?: System.getenv(key)
}