package eg.edu.cu.csds.icare.settings.screen.settings

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.domain.model.SettingsItem
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.util.appRate
import eg.edu.cu.csds.icare.core.ui.util.appShare
import eg.edu.cu.csds.icare.core.ui.util.changeLanguage
import eg.edu.cu.csds.icare.core.ui.util.contactUs
import eg.edu.cu.csds.icare.core.ui.util.currentLanguage
import eg.edu.cu.csds.icare.core.ui.util.reportError
import eg.edu.cu.csds.icare.core.ui.util.showOurApps
import eg.edu.cu.csds.icare.core.ui.util.showPrivacyPolicy
import eg.edu.cu.csds.icare.settings.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SettingsScreen(
    context: Context = LocalContext.current,
    settingsViewModel: SettingsViewModel = koinViewModel(),
    navigateToScreen: (Screen) -> Unit,
) {
    val settings by settingsViewModel.settingsFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    SideEffect {
        scope.launch {
            settingsViewModel.getSettingsItems(context)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .background(color = backgroundColor)
                    .padding(it),
        ) {
            SettingsContent(
                settings = settings,
                selectedLanguage = currentLanguage.ordinal,
                onLanguageChanged = { language -> changeLanguage(language) },
            ) { settingsItem ->
                onItemClicked(
                    settingsItem,
                    context,
                    navigateToScreen = { navigateToScreen(it) },
                )
            }
        }
    }
}

private fun onItemClicked(
    settingsItem: SettingsItem,
    context: Context,
    navigateToScreen: (Screen) -> Unit,
) {
    when (settingsItem.id) {
        Constants.SHARE_CODE -> context.appShare()
        Constants.RATE_CODE -> context.appRate()
        Constants.CONTACT_CODE -> context.contactUs()
        Constants.ERROR_REPORTING_CODE -> context.reportError()
        Constants.APPS_CODE -> context.showOurApps()
        Constants.POLICY_CODE -> context.showPrivacyPolicy()
        Constants.ABOUT_CODE -> navigateToScreen(Screen.About)
    }
}
