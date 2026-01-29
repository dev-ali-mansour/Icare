package eg.edu.cu.csds.icare.feature.admin.screen.center

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import eg.edu.cu.csds.icare.core.ui.common.CenterTypeItem
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.dropDownTextColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.feature.admin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CenterDetailsContent(
    uiState: CenterState,
    modifier: Modifier = Modifier,
    onIntent: (CenterIntent) -> Unit,
) {
    val types = listOf(CenterTypeItem.ImagingCenter, CenterTypeItem.LabCenter)

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(bottom = XL_PADDING)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = uiState.name,
            onValueChange = { onIntent(CenterIntent.UpdateName(it)) },
            label = {
                Text(
                    text = stringResource(eg.edu.cu.csds.icare.core.ui.R.string.core_ui_name),
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

        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(fraction = 0.8f),
            expanded = uiState.isTypesExpanded,
            onExpandedChange = {
                onIntent(CenterIntent.UpdateTypesExpanded(it))
            },
        ) {
            OutlinedTextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                readOnly = true,
                value =
                    types.firstOrNull { it.code == uiState.type }?.let {
                        stringResource(it.textResId)
                    } ?: "",
                onValueChange = { },
                label = {
                    Text(
                        text = stringResource(R.string.feature_admin_type),
                        color = dropDownTextColor,
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = uiState.isTypesExpanded,
                    )
                },
                colors =
                    ExposedDropdownMenuDefaults.textFieldColors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                        unfocusedTrailingIconColor = Color.White,
                        focusedTrailingIconColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                    ),
            )

            ExposedDropdownMenu(
                expanded = uiState.isTypesExpanded,
                onDismissRequest = {
                    onIntent(CenterIntent.UpdateTypesExpanded(false))
                },
            ) {
                types.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(it.textResId),
                                color = dropDownTextColor,
                            )
                        },
                        onClick = {
                            onIntent(CenterIntent.UpdateType(it.code))
                            onIntent(CenterIntent.UpdateTypesExpanded(false))
                        },
                    )
                }
            }
        }

        TextField(
            value = uiState.phone,
            onValueChange = { if (it.length < 14) onIntent(CenterIntent.UpdatePhone(it)) },
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
            onValueChange = { onIntent(CenterIntent.UpdateAddress(it)) },
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
            text = stringResource(eg.edu.cu.csds.icare.core.ui.R.string.core_ui_proceed),
            color = buttonBackgroundColor,
            onClick = { onIntent(CenterIntent.Proceed) },
        )
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
internal fun CenterDetailsContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            CenterDetailsContent(
                uiState = CenterState(),
                onIntent = {},
            )
        }
    }
}
