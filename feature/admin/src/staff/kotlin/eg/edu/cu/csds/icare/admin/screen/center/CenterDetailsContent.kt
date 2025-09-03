package eg.edu.cu.csds.icare.admin.screen.center

import android.content.res.Configuration
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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.core.ui.common.CenterTypeItem
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.dropDownTextColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CenterDetailsContent(
    uiState: CenterState,
    modifier: Modifier = Modifier,
    onEvent: (CenterEvent) -> Unit,
) {
    val types = listOf(CenterTypeItem.ImagingCenter, CenterTypeItem.LabCenter)

    ConstraintLayout(
        modifier =
            modifier
                .fillMaxSize()
                .padding(bottom = XL_PADDING),
    ) {
        val (card) = createRefs()
        Surface(
            modifier =
                Modifier
                    .constrainAs(card) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = uiState.name,
                    onValueChange = { onEvent(CenterEvent.UpdateName(it)) },
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
                        onEvent(CenterEvent.UpdateTypesExpanded(it))
                    },
                ) {
                    OutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
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
                                focusedContainerColor = barBackgroundColor,
                                unfocusedContainerColor = barBackgroundColor,
                                unfocusedTrailingIconColor = Color.White,
                                focusedTrailingIconColor = Color.White,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                            ),
                    )

                    ExposedDropdownMenu(
                        expanded = uiState.isTypesExpanded,
                        onDismissRequest = {
                            onEvent(CenterEvent.UpdateTypesExpanded(false))
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
                                    onEvent(CenterEvent.UpdateType(it.code))
                                    onEvent(CenterEvent.UpdateTypesExpanded(false))
                                },
                            )
                        }
                    }
                }

                TextField(
                    value = uiState.phone,
                    onValueChange = { if (it.length < 14) onEvent(CenterEvent.UpdatePhone(it)) },
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
                    onValueChange = { onEvent(CenterEvent.UpdateAddress(it)) },
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
                    onClick = { onEvent(CenterEvent.Proceed) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun CenterDetailsContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        CenterDetailsContent(
            uiState = CenterState(),
            onEvent = {},
        )
    }
}
