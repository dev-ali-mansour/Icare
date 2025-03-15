package eg.edu.cu.csds.icare.settings.screen.settings

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eg.edu.cu.csds.icare.core.domain.model.Language
import eg.edu.cu.csds.icare.core.domain.model.SettingsItem
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.ui.theme.SETTINGS_ITEM_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.currentLanguage
import eg.edu.cu.csds.icare.core.ui.util.getSettingsItems
import eg.edu.cu.csds.icare.core.ui.view.RadioButtonGroupWithHeader
import eg.edu.cu.csds.icare.settings.R
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
internal fun SettingsContent(
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
                    Constants.CHANGE_LANGUAGE_CODE ->
                        RadioButtonGroupWithHeader(
                            stringResource(id = CoreR.string.choose_app_lang),
                            radioOptions =
                                listOf(
                                    stringResource(id = CoreR.string.radio_lang_english),
                                    stringResource(id = CoreR.string.radio_lang_arabic),
                                ),
                            selected = selectedLanguage,
                        ) { languageCode ->
                            if (currentLanguage == Language.entries.toTypedArray()[languageCode]) {
                                return@RadioButtonGroupWithHeader
                            }
                            onLanguageChanged(Language.entries[languageCode])
                        }

                    else ->
                        SettingItemView(
                            settingsItem = settingsItem,
                            onItemClicked = onItemClicked,
                        )
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
                    .fillMaxWidth(),
            color = contentBackgroundColor,
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
                        color = contentColor,
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
                        modifier = Modifier.size(45.dp),
                        painter = painterResource(id = R.drawable.ic_arrow),
                        contentDescription = null,
                        tint = contentColor,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true)
@Preview(locale = "ar", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
internal fun SettingsContentPreview() {
    val context = LocalContext.current
    Box(modifier = Modifier.background(color = backgroundColor)) {
        SettingsContent(
            settings = context.getSettingsItems(),
            selectedLanguage = 0,
            onLanguageChanged = {},
        ) {}
    }
}
