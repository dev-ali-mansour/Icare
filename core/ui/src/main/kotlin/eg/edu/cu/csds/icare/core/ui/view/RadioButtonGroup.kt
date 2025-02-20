package eg.edu.cu.csds.icare.core.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.LANGUAGE_CARD_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

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
                    .height(LANGUAGE_CARD_HEIGHT)
                    .fillMaxWidth(),
            color = contentBackgroundColor,
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
                    color = contentColor,
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
                                modifier =
                                    Modifier
                                        .fillMaxWidth(),
                                color = contentColor,
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

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RadioButtonGroupWithHeaderPreview() {
    RadioButtonGroupWithHeader(
        stringResource(id = R.string.choose_app_lang),
        radioOptions =
            listOf(
                stringResource(id = R.string.radio_lang_english),
                stringResource(id = R.string.radio_lang_arabic),
            ),
        selected = 0,
    ) {}
}
