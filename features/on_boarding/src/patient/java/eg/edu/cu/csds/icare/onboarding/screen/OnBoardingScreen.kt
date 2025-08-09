package eg.edu.cu.csds.icare.onboarding.screen

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import eg.edu.cu.csds.icare.core.domain.util.Constants.LAST_ON_BOARDING_PAGE
import eg.edu.cu.csds.icare.core.domain.util.Constants.ON_BOARDING_PAGE_COUNT
import eg.edu.cu.csds.icare.core.ui.common.OnBoardingPage
import eg.edu.cu.csds.icare.core.ui.theme.PAGING_INDICATOR_SPACING
import eg.edu.cu.csds.icare.core.ui.theme.PAGING_INDICATOR_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.activeIndicatorColor
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.descriptionColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.inactiveIndicatorColor
import eg.edu.cu.csds.icare.core.ui.theme.titleColor
import eg.edu.cu.csds.icare.core.ui.view.HorizontalPagerIndicator
import eg.edu.cu.csds.icare.core.ui.view.VerticalPagerIndicator
import eg.edu.cu.csds.icare.onboarding.R
import org.koin.androidx.compose.koinViewModel
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
internal fun OnBoardingScreen(
    configuration: Configuration = LocalConfiguration.current,
    onBoardingViewModel: OnBoardingViewModel = koinViewModel(),
    onCompleted: () -> Unit,
) {
    val pagerState = rememberPagerState { ON_BOARDING_PAGE_COUNT }

    val pages =
        listOf(
            OnBoardingPage(
                image = R.drawable.first_page_image,
                title = stringResource(R.string.first_page_title),
                description = stringResource(R.string.first_page_description),
            ),
            OnBoardingPage(
                image = R.drawable.second_page_image,
                title = stringResource(R.string.second_page_title),
                description = stringResource(R.string.second_page_description),
            ),
            OnBoardingPage(
                image = R.drawable.third_page_image,
                title = stringResource(R.string.third_page_title),
                description = stringResource(R.string.third_page_description),
            ),
        )

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE ->
            WelcomeScreenInLandscape(backgroundColor, pagerState, pages) {
                onFinishClicked(
                    onBoardingViewModel = onBoardingViewModel,
                    onCompleted = { onCompleted() },
                )
            }

        else ->
            WelcomeScreenInPortrait(backgroundColor, pagerState, pages) {
                onFinishClicked(
                    onBoardingViewModel = onBoardingViewModel,
                    onCompleted = { onCompleted() },
                )
            }
    }
}

private fun onFinishClicked(
    onBoardingViewModel: OnBoardingViewModel,
    onCompleted: () -> Unit,
) {
    onBoardingViewModel.saveOnBoardingState(completed = true)
    onCompleted()
}

@Composable
private fun WelcomeScreenInPortrait(
    screenBackgroundColor: Color,
    pagerState: PagerState,
    pages: List<OnBoardingPage>,
    onFinishClicked: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(screenBackgroundColor),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(weight = 10f),
            verticalAlignment = Alignment.Top,
        ) { position ->
            PagerScreen(onBoardingPage = pages[position])
        }
        HorizontalPagerIndicator(
            modifier =
                Modifier
                    .weight(weight = 1f)
                    .align(CenterHorizontally),
            pagerState = pagerState,
            activeColor = activeIndicatorColor,
            inactiveColor = inactiveIndicatorColor,
            indicatorWidth = PAGING_INDICATOR_WIDTH,
            spacing = PAGING_INDICATOR_SPACING,
        )
        FinishButton(
            modifier = Modifier.weight(weight = 1f),
            pagerState = pagerState,
        ) { onFinishClicked() }
    }
}

@Composable
private fun WelcomeScreenInLandscape(
    screenBackgroundColor: Color,
    pagerState: PagerState,
    pages: List<OnBoardingPage>,
    onFinishClicked: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(screenBackgroundColor),
    ) {
        Row(
            modifier =
                Modifier
                    .weight(weight = 4f)
                    .fillMaxWidth(),
        ) {
            VerticalPager(
                modifier = Modifier.weight(weight = 10f),
                state = pagerState,
                horizontalAlignment = Alignment.Start,
            ) { position ->
                PagerScreen(onBoardingPage = pages[position])
            }
            VerticalPagerIndicator(
                modifier =
                    Modifier
                        .weight(1f)
                        .align(CenterVertically),
                pagerState = pagerState,
                activeColor = activeIndicatorColor,
                inactiveColor = inactiveIndicatorColor,
                indicatorWidth = PAGING_INDICATOR_WIDTH,
                spacing = PAGING_INDICATOR_SPACING,
            )
        }
        FinishButton(
            modifier = Modifier.weight(weight = 1f),
            pagerState = pagerState,
        ) { onFinishClicked() }
    }
}

@Composable
internal fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.5f)
                    .fillMaxHeight(fraction = 0.5f),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = onBoardingPage.title,
            fontFamily = helveticaFamily,
            color = titleColor,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = XL_PADDING)
                    .padding(top = S_PADDING),
            text = onBoardingPage.description,
            fontFamily = helveticaFamily,
            color = descriptionColor,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
internal fun FinishButton(
    modifier: Modifier,
    pagerState: PagerState,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = XL_PADDING),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center,
    ) {
        AnimatedVisibility(
            modifier = modifier.fillMaxWidth(),
            visible = pagerState.currentPage == LAST_ON_BOARDING_PAGE,
        ) {
            Button(
                onClick = onClick,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = buttonBackgroundColor,
                        contentColor = Color.White,
                    ),
            ) {
                Text(
                    text = stringResource(CoreR.string.finish),
                    fontFamily = helveticaFamily,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, device = Devices.AUTOMOTIVE_1024p)
@Preview(showBackground = true, locale = "ar", device = Devices.AUTOMOTIVE_1024p)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun OnBoardingScreenPreview() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(backgroundColor),
    ) {
        PagerScreen(
            onBoardingPage =
                OnBoardingPage(
                    image = R.drawable.first_page_image,
                    title = stringResource(R.string.first_page_title),
                    description = stringResource(R.string.first_page_description),
                ),
        )
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, device = Devices.AUTOMOTIVE_1024p)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun FinishButtonPreview() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(S_PADDING),
    ) {
        FinishButton(
            modifier = Modifier.fillMaxWidth(),
            pagerState = rememberPagerState { ON_BOARDING_PAGE_COUNT },
        ) { }
    }
}
