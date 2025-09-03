package eg.edu.cu.csds.icare.auth.screen.profile

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.auth.R
import eg.edu.cu.csds.icare.auth.util.handleSignIn
import eg.edu.cu.csds.icare.core.domain.model.Language
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.BOARDER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.Blue700
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.MEDIUM_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.PROFILE_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.currentLanguage
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import timber.log.Timber
import kotlin.system.exitProcess

@Composable
internal fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    request: GetCredentialRequest = koinInject(),
    credentialManager: CredentialManager = koinInject(),
    context: Context = LocalContext.current,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.processEvent(ProfileEvent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is ProfileEffect.SignOutSuccess -> {
                    delay(timeMillis = 100)
                    exitProcess(0)
                }

                is ProfileEffect.ShowError -> {
                    alertMessage = effect.message.asString(context)
                    scope.launch {
                        showAlert = true
                        delay(timeMillis = 3000)
                        showAlert = false
                    }
                }
            }
        },
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) {
        Box(
            modifier =
                Modifier
                    .background(color = backgroundColor)
                    .padding(it),
        ) {
            ProfileContent(
                state = state,
                onEvent = { intent ->
                    scope.launch {
                        when (intent) {
                            is ProfileEvent.LinkWithGoogle -> {
                                val result =
                                    credentialManager.getCredential(request = request, context = context)
                                handleSignIn(
                                    result,
                                    onSuccess = { token ->
                                        viewModel.processEvent(ProfileEvent.UpdateGoogleSignInToken(token))
                                        viewModel.processEvent(intent)
                                    },
                                    onError = { error ->
                                        Timber.e("Google Sign-In failed: $error")
                                    },
                                )
                            }

                            else -> viewModel.processEvent(intent)
                        }
                    }
                },
            )

            if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
        }
    }
}

@Composable
private fun ProfileContent(
    state: ProfileState,
    onEvent: (profileEvent: ProfileEvent) -> Unit,
    context: Context = LocalContext.current,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (progress, infoContainer, logout) = createRefs()
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier =
                    Modifier.constrainAs(progress) {
                        top.linkTo(parent.top, margin = S_PADDING)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
            )
        }

        CurrentUserInfo(
            modifier =
                Modifier.constrainAs(infoContainer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            user = state.currentUser,
            context = context,
            onGoogleClicked = { onEvent(it) },
        )

        AnimatedButton(
            modifier =
                Modifier
                    .constrainAs(logout) {
                        start.linkTo(parent.start, margin = L_PADDING)
                        end.linkTo(parent.end, margin = L_PADDING)
                        bottom.linkTo(parent.bottom, margin = L_PADDING)
                        width = Dimension.fillToConstraints
                    },
            text = stringResource(id = R.string.feature_auth_sign_out),
            color = Color.Red.copy(alpha = 0.6f),
            onClick = { onEvent(ProfileEvent.SignOut) },
        )
    }
}

@Composable
private fun CurrentUserInfo(
    user: User,
    onGoogleClicked: (ProfileEvent) -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
) {
    ConstraintLayout(
        modifier =
            modifier
                .background(contentBackgroundColor)
                .fillMaxWidth()
                .padding(S_PADDING),
    ) {
        val (image, name, email, socialContainer, verified, google) = createRefs()

        Image(
            modifier =
                Modifier
                    .padding(XS_PADDING)
                    .clip(CircleShape)
                    .border(BOARDER_SIZE, Color.DarkGray, CircleShape)
                    .size(PROFILE_IMAGE_SIZE)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
            painter =
                rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(context)
                        .data(data = user.photoUrl)
                        .placeholder(R.drawable.feature_auth_user_placeholder)
                        .error(R.drawable.feature_auth_user_placeholder)
                        .build(),
                ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )

        Text(
            modifier =
                Modifier.constrainAs(name) {
                    top.linkTo(image.top)
                    start.linkTo(image.end, margin = XS_PADDING)
                    end.linkTo(parent.end)
                },
            text = user.displayName,
            color = contentColor,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = helveticaFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier =
                Modifier.constrainAs(email) {
                    top.linkTo(name.bottom, margin = XS_PADDING)
                    start.linkTo(image.end)
                    end.linkTo(parent.end)
                },
            text = user.email,
            color = contentColor,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = helveticaFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        ConstraintLayout(
            modifier =
                Modifier.constrainAs(socialContainer) {
                    top.linkTo(image.bottom, margin = S_PADDING)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
        ) {
            Box(
                modifier =
                    Modifier
                        .constrainAs(verified) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        },
            ) {
                Icon(
                    modifier = Modifier.size(MEDIUM_ICON_SIZE),
                    painter = painterResource(R.drawable.feature_auth_baseline_verified_user_24),
                    contentDescription = null,
                    tint = Color.Green,
                )
            }

            Box(
                modifier =
                    Modifier
                        .constrainAs(google) {
                            top.linkTo(verified.top)
                            start.linkTo(verified.end)
                        }.clickable {
                            if (user.linkedWithGoogle) {
                                onGoogleClicked(ProfileEvent.UnlinkWithGoogle)
                            } else {
                                onGoogleClicked(ProfileEvent.LinkWithGoogle)
                            }
                        },
            ) {
                if (user.linkedWithGoogle) {
                    Icon(
                        modifier = Modifier.size(MEDIUM_ICON_SIZE),
                        painter =
                            painterResource(
                                eg.edu.cu.csds.icare.core.ui.R.drawable.core_ui_ic_social_google,
                            ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.feature_auth_link_google),
                        color = Blue700,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = helveticaFamily,
                        style = TextStyle(textDecoration = TextDecoration.Underline),
                        maxLines = 1,
                    )
                }
            }
            when (currentLanguage) {
                Language.ARABIC ->
                    createHorizontalChain(
                        google,
                        verified,
                        chainStyle = ChainStyle.Spread,
                    )

                else ->
                    createHorizontalChain(
                        verified,
                        google,
                        chainStyle = ChainStyle.Spread,
                    )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "Dark")
@Preview(locale = "ar", showBackground = true, name = "Arabic Light")
@Preview(locale = "ar", showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "Arabic Dark")
@Composable
private fun ProfileContentPreview() {
    Column(modifier = Modifier.background(color = backgroundColor)) {
        ProfileContent(
            state = ProfileState(),
            onEvent = {},
        )
    }
}
