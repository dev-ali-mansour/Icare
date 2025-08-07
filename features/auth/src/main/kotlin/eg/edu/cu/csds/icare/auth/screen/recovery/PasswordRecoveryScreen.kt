package eg.edu.cu.csds.icare.auth.screen.recovery

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.auth.R
import eg.edu.cu.csds.icare.core.ui.theme.Blue500
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL4_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL5_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PasswordRecoveryScreen(
    viewModel: PasswordRecoveryViewModel = koinViewModel(),
    onSignInClicked: () -> Unit,
    onRecoveryCompleted: () -> Unit,
    context: Context = LocalContext.current,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->

        LaunchedEffect(Unit) {
            viewModel.singleEvent.collect { event ->
                when (event) {
                    PasswordRecoverySingleEvent.RecoverySuccess -> onRecoveryCompleted()
                    is PasswordRecoverySingleEvent.ShowInfo -> {
                        alertMessage = context.getString(R.string.recovery_email_sent)
                        showAlert = true
                        delay(timeMillis = 3000)
                        showAlert = false
                        onRecoveryCompleted()
                    }

                    is PasswordRecoverySingleEvent.ShowError -> {
                        alertMessage = event.message.asString(context)
                        scope.launch {
                            showAlert = true
                            delay(timeMillis = 3000)
                            showAlert = false
                        }
                    }
                }
            }
        }

        Box(
            modifier =
                Modifier
                    .background(color = backgroundColor)
                    .fillMaxSize()
                    .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            PasswordRecoveryContent(
                state = state,
                onAction = { intent ->
                    when (intent) {
                        is PasswordRecoveryIntent.NavigateToSignInScreen -> onSignInClicked()
                        else -> viewModel.processIntent(intent)
                    }
                },
            )

            if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
        }
    }
}

@Composable
private fun PasswordRecoveryContent(
    state: PasswordRecoveryState,
    onAction: (PasswordRecoveryIntent) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {
        val (topBox, card, loading) = createRefs()
        Box(
            modifier =
                Modifier
                    .constrainAs(topBox) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }.fillMaxWidth()
                    .height(300.dp)
                    .background(
                        brush =
                            Brush.verticalGradient(
                                startY = 0.0f,
                                colors =
                                    listOf(
                                        Blue500,
                                        Blue500,
                                        Color.White,
                                    ),
                            ),
                    ),
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (txtLogin) = createRefs()
                Text(
                    text = stringResource(id = R.string.forgot_password),
                    modifier =
                        Modifier.constrainAs(txtLogin) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom, margin = XL_PADDING)
                            start.linkTo(parent.start, S_PADDING)
                        },
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = helveticaFamily,
                    color = Color.White,
                )
            }
        }
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .constrainAs(card) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 230.dp)
                        height = Dimension.fillToConstraints
                    }.clip(
                        RoundedCornerShape(
                            topStart = XL4_PADDING,
                            topEnd = XL4_PADDING,
                        ),
                    ),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(backgroundColor),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(XL5_PADDING))
                Surface(
                    modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                    color = backgroundColor,
                    tonalElevation = L_PADDING,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(L_PADDING)),
                    ) {
                        Spacer(modifier = Modifier.padding(L_PADDING))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            TextField(
                                value = state.email,
                                onValueChange = { onAction(PasswordRecoveryIntent.UpdateEmail(it)) },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.email),
                                        fontFamily = helveticaFamily,
                                        color = textColor,
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(fraction = 0.8f),
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
                                        autoCorrectEnabled = true,
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Done,
                                    ),
                            )

                            Spacer(modifier = Modifier.height(L_PADDING))
                            AnimatedButton(
                                modifier =
                                    Modifier
                                        .fillMaxWidth(fraction = 0.8f),
                                text = stringResource(id = R.string.reset),
                                color = buttonBackgroundColor,
                                onClick = { onAction(PasswordRecoveryIntent.SubmitRecovery) },
                            )

                            Text(
                                text = stringResource(R.string.sign_in),
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontFamily = helveticaFamily,
                                color = textColor,
                                modifier =
                                    Modifier
                                        .padding(L_PADDING)
                                        .clickable { onAction(PasswordRecoveryIntent.NavigateToSignInScreen) },
                            )
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier =
                    Modifier.constrainAs(loading) {
                        top.linkTo(parent.top, margin = L_PADDING)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
private fun PasswordRecoveryContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        PasswordRecoveryContent(
            state = PasswordRecoveryState(),
            onAction = {},
        )
    }
}
