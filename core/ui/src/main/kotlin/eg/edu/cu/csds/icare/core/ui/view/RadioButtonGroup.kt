package eg.edu.cu.csds.icare.core.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.LANGUAGE_CARD_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.MAX_SURFACE_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark

@Composable
fun RadioButtonGroupWithHeader(
    header: String,
    radioOptions: List<String>,
    selected: Int,
    onItemSelected: (Int) -> Unit,
) {
    Box(contentAlignment = Alignment.CenterStart) {
        var selectedOption by remember { mutableIntStateOf(selected) }
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = MAX_SURFACE_WIDTH)
                    .height(LANGUAGE_CARD_HEIGHT),
            shape =
                RoundedCornerShape(
                    topStart = L_PADDING,
                    topEnd = L_PADDING,
                    bottomStart = L_PADDING,
                    bottomEnd = L_PADDING,
                ),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(M_PADDING)
                        .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = header,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = helveticaFamily,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    radioOptions.forEachIndexed { index, text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (index == selectedOption),
                                    onClick = {
                                        selectedOption = index
                                        onItemSelected(index)
                                    },
                                ),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = (index == selectedOption),
                                modifier = Modifier.padding(horizontal = XS_PADDING),
                                onClick = {
                                    selectedOption = index
                                    onItemSelected(index)
                                },
                            )
                            Text(
                                text = text,
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                fontWeight = FontWeight.Bold,
                                fontFamily = helveticaFamily,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
fun RadioButtonGroupWithHeaderPreview() {
    IcareTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(M_PADDING)
                    .background(color = MaterialTheme.colorScheme.background),
        ) {
            RadioButtonGroupWithHeader(
                stringResource(id = R.string.core_ui_choose_app_lang),
                radioOptions =
                    listOf(
                        stringResource(id = R.string.core_ui_radio_lang_english),
                        stringResource(id = R.string.core_ui_radio_lang_arabic),
                    ),
                selected = 0,
            ) {}
        }
    }
}
