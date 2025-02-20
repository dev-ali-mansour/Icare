package eg.edu.cu.csds.icare.core.domain.model

enum class Language(
    val code: String,
) {
    ENGLISH("en"),
    ARABIC("ar"),
}

fun String.toLanguage(): Language = Language.entries.firstOrNull { it.code == this } ?: Language.ENGLISH
