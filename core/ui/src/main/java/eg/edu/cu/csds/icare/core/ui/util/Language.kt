package eg.edu.cu.csds.icare.core.ui.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import eg.edu.cu.csds.icare.core.domain.model.Language
import eg.edu.cu.csds.icare.core.domain.model.toLanguage
import java.util.Locale

val currentLanguage: Language
    get() =
        AppCompatDelegate.getApplicationLocales()[0]?.language?.toLanguage()
            ?: Locale.getDefault().language.toLanguage()

fun changeLanguage(language: Language) {
    AppCompatDelegate.setApplicationLocales(
        LocaleListCompat.forLanguageTags(language.code),
    )
}
