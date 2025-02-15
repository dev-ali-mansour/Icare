package extensions

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project
import java.util.Properties

private const val LOCAL_PROPERTIES_FILE_NAME = "dev_credentials.properties"

fun Project.getLocalProperty(propertyName: String): String {
    val localProperties =
        Properties().apply {
            val localPropertiesFile = rootProject.file(LOCAL_PROPERTIES_FILE_NAME)
            if (localPropertiesFile.exists()) {
                load(localPropertiesFile.inputStream())
            }
        }
    return localProperties.getProperty(propertyName)
        ?: throw NoSuchFieldException("Property $propertyName not found in $LOCAL_PROPERTIES_FILE_NAME")
}

val osFamily: String
    get() =
        when {
            Os.isFamily(Os.FAMILY_WINDOWS) -> Os.FAMILY_WINDOWS
            Os.isFamily(Os.FAMILY_UNIX) -> Os.FAMILY_UNIX
            else -> ""
        }
