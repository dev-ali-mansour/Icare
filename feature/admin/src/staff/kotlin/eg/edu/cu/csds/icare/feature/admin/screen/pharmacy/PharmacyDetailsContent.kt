package eg.edu.cu.csds.icare.feature.admin.screen.pharmacy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.feature.admin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PharmacyDetailsContent(
    uiState: PharmacyState,
    modifier: Modifier = Modifier,
    onIntent: (PharmacyIntent) -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = uiState.name,
            onValueChange = { onIntent(PharmacyIntent.UpdateName(it)) },
            label = {
                Text(
                    text = stringResource(string.core_ui_name),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )
            },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.8f)
                    .padding(top = L_PADDING),
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    cursorColor = contentColor,
                    focusedTextColor = textColor,
                    focusedIndicatorColor = Yellow500,
                    unfocusedIndicatorColor = Yellow500.copy(alpha = 0.38f),
                ),
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
        )

        TextField(
            value = uiState.phone,
            onValueChange = { if (it.length < 14) onIntent(PharmacyIntent.UpdatePhone(it)) },
            label = {
                Text(
                    text = stringResource(R.string.feature_admin_phone_number),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )
            },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.8f),
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    cursorColor = contentColor,
                    focusedTextColor = textColor,
                    focusedIndicatorColor = Yellow500,
                    unfocusedIndicatorColor = Yellow500.copy(alpha = 0.38f),
                ),
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next,
                ),
        )

        TextField(
            value = uiState.address,
            onValueChange = { onIntent(PharmacyIntent.UpdateAddress(it)) },
            label = {
                Text(
                    text = stringResource(R.string.feature_admin_address),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )
            },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.8f),
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    cursorColor = contentColor,
                    focusedTextColor = textColor,
                    focusedIndicatorColor = Yellow500,
                    unfocusedIndicatorColor = Yellow500.copy(alpha = 0.38f),
                ),
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
        )

        Spacer(modifier = Modifier.height(L_PADDING))

        AnimatedButton(
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.6f),
            text = stringResource(string.core_ui_proceed),
            color = buttonBackgroundColor,
            onClick = { onIntent(PharmacyIntent.Proceed) },
        )
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
internal fun PharmacyDetailsContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            PharmacyDetailsContent(
                uiState =
                    PharmacyState(
                        id = 1L,
                        name = "صيدلية الدقم",
                        phone = "01234567890",
                        address = "43 ش أحمد ماهر",
                    ),
                onIntent = {},
            )
        }
    }
}
