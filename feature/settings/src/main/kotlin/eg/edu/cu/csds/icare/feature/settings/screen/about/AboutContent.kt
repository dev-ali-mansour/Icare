package eg.edu.cu.csds.icare.feature.settings.screen.about

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.core.domain.model.Language
import eg.edu.cu.csds.icare.core.ui.R.drawable
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.theme.LOGO_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.LOGO_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.SOCIAL_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.util.currentLanguage
import eg.edu.cu.csds.icare.core.ui.util.openLink

@Composable
internal fun AboutContent(appVersion: String) {
    val context: Context = LocalContext.current
    val websiteUrl = stringResource(string.core_ui_website_url)
    val facebookUrl = stringResource(string.core_ui_facebook_url)

    ConstraintLayout(
        modifier =
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
    ) {
        val (version, about, made, logo, socialContainer, facebook, twitter, linkedIn, youtube) =
            createRefs()
        Text(
            modifier =
                Modifier.constrainAs(version) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            text = "${
                stringResource(
                    id = string.core_ui_version,
                )
            } $appVersion",
            color = textColor,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontFamily = helveticaFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier =
                Modifier.constrainAs(about) {
                    top.linkTo(version.bottom)
                    start.linkTo(version.start)
                },
            text = stringResource(id = string.core_ui_about),
            color = textColor.copy(alpha = 0.6f),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = helveticaFamily,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .constrainAs(made) {
                        top.linkTo(about.bottom)
                        start.linkTo(version.start)
                    },
            text = stringResource(string.core_ui_made_by),
            color = textColor,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontFamily = helveticaFamily,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
        )

        Image(
            modifier =
                Modifier
                    .size(LOGO_WIDTH, LOGO_HEIGHT)
                    .constrainAs(logo) {
                        top.linkTo(made.bottom, margin = S_PADDING)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }.clickable {
                        context.openLink(websiteUrl)
                    },
            painter = painterResource(id = drawable.core_ui_logo),
            contentScale = ContentScale.Fit,
            contentDescription = null,
        )

        ConstraintLayout(
            modifier =
                Modifier.constrainAs(socialContainer) {
                    top.linkTo(logo.bottom, margin = L_PADDING)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
        ) {
            Image(
                modifier =
                    Modifier
                        .size(SOCIAL_ICON_SIZE)
                        .constrainAs(facebook) {
                            top.linkTo(logo.bottom, margin = S_PADDING)
                            start.linkTo(parent.start)
                        }.clickable {
                            context.openLink(facebookUrl)
                        },
                painter =
                    painterResource(
                        id = drawable.core_ui_ic_social_facebook,
                    ),
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )

            Image(
                modifier =
                    Modifier
                        .size(SOCIAL_ICON_SIZE)
                        .constrainAs(twitter) {
                            top.linkTo(facebook.top)
                            start.linkTo(facebook.end)
                        }.clickable {
                            // context.openLink(context.getString(AppText.twitter_url))
                        },
                painter =
                    painterResource(
                        id = drawable.core_ui_ic_social_twitter,
                    ),
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )

            Image(
                modifier =
                    Modifier
                        .size(SOCIAL_ICON_SIZE)
                        .constrainAs(linkedIn) {
                            top.linkTo(facebook.top)
                            start.linkTo(twitter.end)
                        }.clickable {
                            // context.openLink(context.getString(AppText.linkedin_url))
                        },
                painter =
                    painterResource(
                        id = drawable.core_ui_ic_social_linkedin,
                    ),
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )
            Image(
                modifier =
                    Modifier
                        .constrainAs(youtube) {
                            top.linkTo(facebook.top)
                            start.linkTo(linkedIn.end)
                            end.linkTo(parent.end)
                        }.size(SOCIAL_ICON_SIZE)
                        .clickable {
                            // context.openLink(context.getString(AppText.youtube_channel_url))
                        },
                painter =
                    painterResource(
                        id = drawable.core_ui_ic_social_youtube,
                    ),
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )
            when (currentLanguage) {
                Language.ARABIC -> {
                    createHorizontalChain(
                        youtube,
                        linkedIn,
                        twitter,
                        facebook,
                        chainStyle = ChainStyle.Spread,
                    )
                }

                else -> {
                    createHorizontalChain(
                        facebook,
                        twitter,
                        linkedIn,
                        youtube,
                        chainStyle = ChainStyle.Spread,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun AboutContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        AboutContent(appVersion = "1.0.0")
    }
}
