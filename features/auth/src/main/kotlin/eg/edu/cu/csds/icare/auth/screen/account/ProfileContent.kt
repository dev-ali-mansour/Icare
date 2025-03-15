package eg.edu.cu.csds.icare.auth.screen.account

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.auth.R
import eg.edu.cu.csds.icare.core.domain.model.FirebaseUserBasicInfo
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.ui.theme.BOARDER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROFILE_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton

@Composable
internal fun ProfileContent(
    userResource: Resource<User>,
    logoutRes: Resource<Nothing?>,
    user: FirebaseUserBasicInfo?,
    isLoading: Boolean,
    onLogoutClicked: () -> Unit,
    onLogoutSucceed: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
    context: Context = LocalContext.current,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (progress, infoContainer, logout) = createRefs()
        if (isLoading) {
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

        user?.let { user ->
            CurrentUserInfo(
                modifier =
                    Modifier.constrainAs(infoContainer) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                user = user,
                context = context,
            )

            when (userResource) {
                is Resource.Unspecified -> {}
                is Resource.Loading ->
                    CircularProgressIndicator(
                        modifier =
                            Modifier.constrainAs(progress) {
                                top.linkTo(parent.top, margin = S_PADDING)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            },
                    )

                is Resource.Success ->
                    userResource.data?.let {
                    }

                is Resource.Error -> {
                    LaunchedEffect(key1 = true) {
                        onError(userResource.error)
                    }
                }
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
                text = stringResource(id = R.string.sign_out),
                color = Color.Red.copy(alpha = 0.6f),
                onClick = { onLogoutClicked() },
            )
        }

        when (logoutRes) {
            is Resource.Unspecified -> {}
            is Resource.Loading ->
                CircularProgressIndicator(
                    modifier =
                        Modifier.constrainAs(progress) {
                            top.linkTo(parent.top, margin = S_PADDING)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                )

            is Resource.Success -> onLogoutSucceed()

            is Resource.Error -> {
                LaunchedEffect(key1 = true) {
                    onError(logoutRes.error)
                }
            }
        }
    }
}

@Composable
internal fun CurrentUserInfo(
    modifier: Modifier,
    user: FirebaseUserBasicInfo,
    context: Context,
) {
    ConstraintLayout(
        modifier =
            modifier
                .background(contentBackgroundColor)
                .fillMaxWidth()
                .padding(S_PADDING),
    ) {
        val (image, name, email) = createRefs()

        Image(
            modifier =
                Modifier
                    .padding(XS_PADDING)
                    .clip(CircleShape)
                    .border(BOARDER_SIZE, Color.DarkGray, CircleShape)
                    .size(PROFILE_ICON_SIZE)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
            painter =
                rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(context)
                        .data(data = user.photoUrl)
                        .placeholder(R.drawable.user_placeholder)
                        .error(R.drawable.user_placeholder)
                        .build(),
                ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )

        Text(
            modifier =
                Modifier
                    .constrainAs(name) {
                        top.linkTo(image.top)
                        start.linkTo(image.end, margin = XS_PADDING)
                        end.linkTo(parent.end)
                    },
            text = user.displayName ?: "",
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
            text = user.email ?: "",
            color = contentColor,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = helveticaFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "Dark")
@Preview(locale = "ar", showBackground = true, name = "Arabic Light")
@Preview(locale = "ar", showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "Arabic Dark")
@Composable
internal fun ProfileContentPreview() {
    Column(modifier = Modifier.background(color = backgroundColor)) {
        ProfileContent(
            userResource = Resource.Success(data = User()),
            logoutRes = Resource.Unspecified(),
            user =
                FirebaseUserBasicInfo(
                    displayName = "Ali Mansour",
                    email = "",
                    photoUrl = "".toUri(),
                ),
            isLoading = false,
            onLogoutClicked = {},
            onLogoutSucceed = {},
            onError = {},
        )
    }
}
