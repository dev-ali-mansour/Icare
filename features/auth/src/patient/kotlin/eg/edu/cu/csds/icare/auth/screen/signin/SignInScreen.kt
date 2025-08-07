package eg.edu.cu.csds.icare.auth.screen.signin

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.auth.R
import eg.edu.cu.csds.icare.auth.util.handleSignIn
import eg.edu.cu.csds.icare.core.ui.theme.Blue500
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL3_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL4_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.core.ui.view.SocialSignInButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import timber.log.Timber
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
internal fun SignInScreen(
    viewModel: SignInViewModel = koinViewModel(),
    onRecoveryClicked: () -> Unit,
    onCreateAnAccountClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
    context: Context = LocalContext.current,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope: CoroutineScope = rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    val request: GetCredentialRequest = koinInject()
    val credentialManager: CredentialManager = koinInject()

    LaunchedEffect(Unit) {
        viewModel.singleEvent.collect { event ->
            when (event) {
                is SignInSingleEvent.LoginSuccess -> onLoginSuccess()
                is SignInSingleEvent.ShowError -> {
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .background(color = backgroundColor)
                    .fillMaxSize()
                    .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            SignInContent(
                state = state,
                onIntent = { intent ->
                    when (intent) {
                        is SignInIntent.SignInWithGoogle -> {
                            scope.launch {
                                runCatching {
                                    val result =
                                        credentialManager.getCredential(
                                            request = request,
                                            context = context,
                                        )
                                    handleSignIn(result, onSuccess = {
                                        viewModel.processIntent(
                                            SignInIntent.UpdateGoogleSignInToken(
                                                it,
                                            ),
                                        )
                                        viewModel.processIntent(SignInIntent.SignInWithGoogle)
                                    }, onError = { error ->
                                        Timber.e("Google Sign-In failed: $error")
                                    })
                                }.onFailure { error ->
                                    Timber.e(error.toString())
                                }
                            }
                        }

                        is SignInIntent.NavigateToPasswordRecoveryScreen -> {
                            onRecoveryClicked()
                        }

                        is SignInIntent.NavigateToSignUpScreen -> {
                            onCreateAnAccountClicked()
                        }

                        else -> {
                            viewModel.processIntent(intent = intent)
                        }
                    }
                },
            )

            if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
        }
    }
}

@Composable
private fun SignInContent(
    state: SignInState,
    onIntent: (SignInIntent) -> Unit,
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
                                        barBackgroundColor,
                                        barBackgroundColor,
                                        Color.White,
                                    ),
                            ),
                    ),
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (txtLogin, image) = createRefs()
                Text(
                    text = stringResource(id = R.string.sign_in),
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
                Image(
                    modifier =
                        Modifier.constrainAs(image) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom, margin = XL_PADDING)
                            start.linkTo(txtLogin.end)
                            end.linkTo(parent.end)
                        },
                    painter = painterResource(id = R.drawable.login),
                    contentDescription = null,
                    alpha = .6f,
                )
            }
        }
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .constrainAs(card) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 230.dp)
                        height = Dimension.fillToConstraints
                    }.clip(RoundedCornerShape(topStart = XL4_PADDING, topEnd = XL4_PADDING)),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(backgroundColor),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(XL3_PADDING))
                Surface(
                    modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                    color = backgroundColor,
                    tonalElevation = L_PADDING,
                ) {
                    Column {
                        TextField(
                            value = state.email,
                            onValueChange = { onIntent(SignInIntent.UpdateEmail(it)) },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.email),
                                    fontFamily = helveticaFamily,
                                    color = textColor,
                                )
                            },
                            singleLine = true,
                            modifier =
                                Modifier
                                    .fillMaxWidth(),
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
                                    imeAction = ImeAction.Next,
                                ),
                        )
                        HorizontalDivider(
                            modifier =
                                Modifier
                                    .height(1.dp)
                                    .background(Color.LightGray),
                        )
                        TextField(
                            value = state.password,
                            onValueChange = { onIntent(SignInIntent.UpdatePassword(it)) },
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
                            trailingIcon = {
                                IconButton(onClick = { onIntent(SignInIntent.TogglePasswordVisibility) }) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_baseline_remove_red_eye_24),
                                        contentDescription = null,
                                        tint = if (state.isPasswordVisible) Blue500 else Color.Gray,
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.password),
                                    fontFamily = helveticaFamily,
                                    color = textColor,
                                )
                            },
                            singleLine = true,
                            visualTransformation =
                                if (state.isPasswordVisible) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions =
                                KeyboardOptions.Default.copy(
                                    autoCorrectEnabled = false,
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done,
                                ),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(S_PADDING))

                Text(
                    text = stringResource(id = R.string.forgot_your_password),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontFamily = helveticaFamily,
                    color = textColor,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = XL_PADDING)
                            .clickable { onIntent(SignInIntent.NavigateToPasswordRecoveryScreen) },
                )

                Spacer(modifier = Modifier.height(S_PADDING))

                AnimatedButton(
                    modifier = Modifier.fillMaxWidth(fraction = 0.6f),
                    text = stringResource(id = R.string.sign_in),
                    color = buttonBackgroundColor,
                    onClick = { onIntent(SignInIntent.SubmitSignIn) },
                )

                Text(
                    text = stringResource(id = R.string.do_not_have_account),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontFamily = helveticaFamily,
                    color = textColor,
                    modifier =
                        Modifier
                            .padding(L_PADDING)
                            .clickable { onIntent(SignInIntent.NavigateToSignUpScreen) },
                )

                Spacer(modifier = Modifier.height(S_PADDING))

                Text(
                    text = stringResource(id = CoreR.string.or),
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontFamily = helveticaFamily,
                    textAlign = TextAlign.Center,
                    color = textColor,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = L_PADDING),
                )

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(fraction = 0.8f)
                            .padding(vertical = S_PADDING),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    SocialSignInButton(
                        modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                        iconId = CoreR.drawable.ic_social_google,
                    ) {
                        onIntent(SignInIntent.SignInWithGoogle)
                    }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier =
                    Modifier.constrainAs(loading) {
                        top.linkTo(parent.top)
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
private fun LoginContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        SignInContent(
            state = SignInState(),
            onIntent = {},
        )
    }
}
