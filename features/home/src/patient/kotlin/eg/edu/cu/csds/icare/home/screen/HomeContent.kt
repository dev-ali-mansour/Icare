package eg.edu.cu.csds.icare.home.screen

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.core.domain.model.FirebaseUserBasicInfo
import eg.edu.cu.csds.icare.core.domain.model.Permission
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.ui.theme.CATEGORY_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.HEADER_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.HEADER_PROFILE_CARD_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.Orange200
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.U_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.Yellow700
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.kufamFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.home.R
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
internal fun HomeContent(
    firebaseUser: FirebaseUserBasicInfo?,
    userResource: Resource<User>,
    appVersion: String,
    onUserClicked: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (progress, titleContainer, line, marquee) = createRefs()
        Surface(
            modifier =
                Modifier.constrainAs(titleContainer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            color = backgroundColor,
            tonalElevation = S_PADDING,
        ) {
            TitleView(
                modifier = Modifier,
                appVersion = appVersion,
                firebaseUser = firebaseUser,
                onUserClicked = { onUserClicked() },
            )
        }
        Box(
            modifier =
                Modifier
                    .constrainAs(line) {
                        top.linkTo(titleContainer.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }.fillMaxWidth()
                    .height(XS_PADDING)
                    .background(Yellow500),
        )

        when (userResource) {
            is Resource.Unspecified -> {}
            is Resource.Loading ->
                CircularProgressIndicator(
                    modifier =
                        Modifier.constrainAs(progress) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                )

            is Resource.Success ->
                userResource.data?.let {
                }

            is Resource.Error ->
                LaunchedEffect(key1 = true) {
                    onError(userResource.error)
                }
        }

        Text(
            text = stringResource(id = CoreR.string.made_by),
            modifier =
                Modifier
                    .constrainAs(marquee) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }.fillMaxWidth(fraction = 0.9f)
                    .basicMarquee(iterations = Int.MAX_VALUE),
            color = Orange200,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = helveticaFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            maxLines = 1,
        )
    }
}

@Composable
private fun TitleView(
    appVersion: String,
    firebaseUser: FirebaseUserBasicInfo?,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onUserClicked: () -> Unit,
) {
    ConstraintLayout(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = S_PADDING, vertical = XS_PADDING),
    ) {
        val (logo, title, version, card) = createRefs()

        Image(
            modifier =
                Modifier
                    .constrainAs(logo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }.size(HEADER_ICON_SIZE),
            painter = painterResource(CoreR.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )

        Text(
            modifier =
                Modifier.constrainAs(title) {
                    start.linkTo(logo.end)
                    end.linkTo(card.end)
                    bottom.linkTo(logo.bottom)
                    width = Dimension.fillToConstraints
                },
            text = stringResource(CoreR.string.app_name),
            fontFamily = kufamFamily,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            color = textColor,
        )

        Surface(
            modifier =
                Modifier
                    .constrainAs(card) {
                        top.linkTo(logo.top, S_PADDING)
                        end.linkTo(parent.end, S_PADDING)
                    }.clickable { onUserClicked() },
            color = contentBackgroundColor,
            shape = RoundedCornerShape(S_PADDING),
        ) {
            ConstraintLayout(
                modifier =
                    Modifier
                        .width(HEADER_PROFILE_CARD_WIDTH)
                        .padding(U_PADDING),
            ) {
                firebaseUser?.let { user ->

                    val (image, name) = createRefs()

                    Image(
                        modifier =
                            Modifier
                                .clip(CircleShape)
                                .size(CATEGORY_ICON_SIZE)
                                .constrainAs(image) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    bottom.linkTo(parent.bottom)
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
                            Modifier.constrainAs(name) {
                                top.linkTo(image.top)
                                start.linkTo(image.end, margin = XS_PADDING)
                                bottom.linkTo(image.bottom)
                            },
                        text = user.displayName ?: "",
                        color = contentColor,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = helveticaFamily,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

        Text(
            text = "V. $appVersion",
            modifier =
                Modifier
                    .constrainAs(version) {
                        top.linkTo(title.top)
                        end.linkTo(parent.end, S_PADDING)
                    },
            color = Yellow700,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = helveticaFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun HomeContentPreview() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(XS_PADDING),
    ) {
        HomeContent(
            firebaseUser =
                FirebaseUserBasicInfo(
                    displayName = "Ali Mansour",
                    email = "",
                    photoUrl = "".toUri(),
                ),
            userResource =
                Resource.Success(
                    User(
                        permissions =
                            listOf(
                                Permission.CreatePrescriptionPermission.code,
                            ),
                    ),
                ),
            appVersion = "1.0.0",
            onUserClicked = {},
            onError = {},
        )
    }
}
