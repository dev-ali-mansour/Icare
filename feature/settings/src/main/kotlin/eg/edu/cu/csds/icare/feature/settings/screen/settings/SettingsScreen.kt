package eg.edu.cu.csds.icare.feature.settings.screen.settings

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.domain.model.Language
import eg.edu.cu.csds.icare.core.domain.model.SettingsItem
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.theme.ACTION_BUTTON_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.MAX_SURFACE_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.SETTINGS_ITEM_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.appRate
import eg.edu.cu.csds.icare.core.ui.util.appShare
import eg.edu.cu.csds.icare.core.ui.util.changeLanguage
import eg.edu.cu.csds.icare.core.ui.util.contactUs
import eg.edu.cu.csds.icare.core.ui.util.currentLanguage
import eg.edu.cu.csds.icare.core.ui.util.getSettingsItems
import eg.edu.cu.csds.icare.core.ui.util.reportError
import eg.edu.cu.csds.icare.core.ui.util.showOurApps
import eg.edu.cu.csds.icare.core.ui.util.showPrivacyPolicy
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.RadioButtonGroupWithHeader
import eg.edu.cu.csds.icare.feature.settings.R
import eg.edu.cu.csds.icare.feature.settings.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SettingsScreen(navigateToScreen: (Route) -> Unit) {
    val settingsViewModel: SettingsViewModel = koinViewModel()
    val context: Context = LocalContext.current
    val settings by settingsViewModel.settingsFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        settingsViewModel.getSettingsItems(context)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.padding(it)) {
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
    navigateToScreen: (Route) -> Unit,
) {
    when (settingsItem.id) {
        Constants.SHARE_CODE -> context.appShare()
        Constants.RATE_CODE -> context.appRate()
        Constants.CONTACT_CODE -> context.contactUs()
        Constants.ERROR_REPORTING_CODE -> context.reportError()
        Constants.APPS_CODE -> context.showOurApps()
        Constants.POLICY_CODE -> context.showPrivacyPolicy()
        Constants.ABOUT_CODE -> navigateToScreen(Route.About)
    }
}

@Composable
private fun SettingsContent(
    settings: List<SettingsItem>,
    selectedLanguage: Int,
    onLanguageChanged: (Language) -> Unit,
    onItemClicked: (SettingsItem) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = S_PADDING),
            verticalArrangement = Arrangement.spacedBy(XS_PADDING),
        ) {
            items(settings) { settingsItem ->
                when (settingsItem.id) {
                    Constants.CHANGE_LANGUAGE_CODE -> {
                        RadioButtonGroupWithHeader(
                            header = stringResource(id = string.core_ui_choose_app_lang),
                            radioOptions =
                                listOf(
                                    stringResource(id = string.core_ui_radio_lang_english),
                                    stringResource(id = string.core_ui_radio_lang_arabic),
                                ),
                            selected = selectedLanguage,
                        ) { languageCode ->
                            if (currentLanguage == Language.entries.toTypedArray()[languageCode]) {
                                return@RadioButtonGroupWithHeader
                            }
                            onLanguageChanged(Language.entries[languageCode])
                        }
                    }

                    else -> {
                        SettingItemView(
                            settingsItem = settingsItem,
                            onItemClicked = onItemClicked,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingItemView(
    settingsItem: SettingsItem,
    onItemClicked: (SettingsItem) -> Unit,
) {
    Box(
        modifier = Modifier.clickable { onItemClicked(settingsItem) },
        contentAlignment = Alignment.CenterStart,
    ) {
        Surface(
            modifier =
                Modifier
                    .height(SETTINGS_ITEM_HEIGHT)
                    .fillMaxWidth()
                    .widthIn(max = MAX_SURFACE_WIDTH),
            shape = RoundedCornerShape(S_PADDING),
        ) {
            Row(
                modifier = Modifier.padding(all = XS_PADDING),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier,
                        text = settingsItem.title,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = helveticaFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        modifier = Modifier.size(ACTION_BUTTON_ICON_SIZE),
                        painter = painterResource(id = R.drawable.feature_settings_ic_arrow),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
private fun SettingsContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            SettingsContent(
                settings = LocalContext.current.getSettingsItems(),
                selectedLanguage = 0,
                onLanguageChanged = {},
            ) {}
        }
    }
}
