package eg.edu.cu.csds.icare.feature.auth.screen.profile

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.core.domain.model.Language
import eg.edu.cu.csds.icare.core.ui.R.drawable
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.BOARDER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.Blue700
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.MEDIUM_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROFILE_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.TabRowContainerColor
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.currentLanguage
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.feature.auth.R
import eg.edu.cu.csds.icare.feature.auth.util.handleSignIn
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import timber.log.Timber

@Composable
internal fun ProfileScreen(onSignOut: () -> Unit) {
    val viewModel: ProfileViewModel = koinViewModel()
    val request: GetCredentialRequest = koinInject()
    val credentialManager: CredentialManager = koinInject()
    val context: Context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(ProfileIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is ProfileEffect.SignOutSuccess -> {
                    onSignOut()
                }

                is ProfileEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message.asString(context),
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        },
    )

    Scaffold(
        topBar = {
            ProfileTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                uiState = uiState,
                onIntent = { intent ->
                    scope.launch {
                        when (intent) {
                            is ProfileIntent.LinkWithGoogle -> {
                                val result =
                                    credentialManager.getCredential(request = request, context = context)
                                handleSignIn(
                                    result,
                                    onSuccess = { token ->
                                        viewModel.handleIntent(ProfileIntent.UpdateGoogleSignInToken(token))
                                        viewModel.handleIntent(intent)
                                    },
                                    onError = { error ->
                                        Timber.e("Google Sign-In failed: $error")
                                    },
                                )
                            }

                            else -> {
                                viewModel.handleIntent(intent)
                            }
                        }
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->

        ProfileContent(
            uiState = uiState,
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(top = M_PADDING),
            onIntent = { intent ->
                scope.launch {
                    when (intent) {
                        is ProfileIntent.LinkWithGoogle -> {
                            val result =
                                credentialManager.getCredential(request = request, context = context)
                            handleSignIn(
                                result,
                                onSuccess = { token ->
                                    viewModel.handleIntent(ProfileIntent.UpdateGoogleSignInToken(token))
                                    viewModel.handleIntent(intent)
                                },
                                onError = { error ->
                                    Timber.e("Google Sign-In failed: $error")
                                },
                            )
                        }

                        else -> {
                            viewModel.handleIntent(intent)
                        }
                    }
                }
            },
        )
    }
}

@Composable
private fun ProfileContent(
    uiState: ProfileState,
    modifier: Modifier = Modifier,
    onIntent: (profileIntent: ProfileIntent) -> Unit,
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (progress, logout) = createRefs()
        if (uiState.isLoading) {
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
            onClick = { onIntent(ProfileIntent.SignOut) },
        )
    }
}

@Composable
private fun ProfileTopAppBar(
    uiState: ProfileState,
    onIntent: (ProfileIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context: Context = LocalContext.current
    Column {
        ConstraintLayout(
            modifier =
                modifier
                    .background(TabRowContainerColor)
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
                            .data(data = uiState.currentUser.photoUrl)
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
                text = uiState.currentUser.displayName,
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
                text = uiState.currentUser.email,
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
                                if (uiState.currentUser.linkedWithGoogle) {
                                    onIntent(ProfileIntent.UnlinkWithGoogle)
                                } else {
                                    onIntent(ProfileIntent.LinkWithGoogle)
                                }
                            },
                ) {
                    if (uiState.currentUser.linkedWithGoogle) {
                        Icon(
                            modifier = Modifier.size(MEDIUM_ICON_SIZE),
                            painter = painterResource(drawable.core_ui_ic_social_google),
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
                    Language.ARABIC -> {
                        createHorizontalChain(
                            google,
                            verified,
                            chainStyle = ChainStyle.Spread,
                        )
                    }

                    else -> {
                        createHorizontalChain(
                            verified,
                            google,
                            chainStyle = ChainStyle.Spread,
                        )
                    }
                }
            }
        }
        Box(
            modifier =
                Modifier
                    .background(Yellow500)
                    .fillMaxWidth()
                    .height(XS_PADDING),
        )
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
private fun ProfileContentPreview() {
    IcareTheme {
        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            ProfileContent(
                uiState = ProfileState(),
                onIntent = {},
            )
        }
    }
}
