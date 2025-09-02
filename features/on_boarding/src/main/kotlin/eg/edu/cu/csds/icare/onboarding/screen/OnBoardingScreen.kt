package eg.edu.cu.csds.icare.onboarding.screen

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.domain.util.Constants.LAST_ON_BOARDING_PAGE
import eg.edu.cu.csds.icare.core.domain.util.Constants.ON_BOARDING_PAGE_COUNT
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
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
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.core.ui.view.HorizontalPagerIndicator
import eg.edu.cu.csds.icare.core.ui.view.VerticalPagerIndicator
import eg.edu.cu.csds.icare.onboarding.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
internal fun OnBoardingScreen(
    onFinished: () -> Unit,
    configuration: Configuration = LocalConfiguration.current,
    viewModel: OnBoardingViewModel = koinViewModel(),
    context: Context = LocalContext.current,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope: CoroutineScope = rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { ON_BOARDING_PAGE_COUNT }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.processEvent(OnBoardingEvent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is OnBoardingEffect.OnBoardingFinished -> {
                    onFinished()
                }

                is OnBoardingEffect.ShowError -> {
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

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE ->
            WelcomeScreenInLandscape(backgroundColor, pagerState, pages) {
                viewModel.processEvent(OnBoardingEvent.FinishOnBoarding)
            }

        else ->
            WelcomeScreenInPortrait(backgroundColor, pagerState, pages) {
                viewModel.processEvent(OnBoardingEvent.FinishOnBoarding)
            }
    }

    if (uiState.isLoading) CircularProgressIndicator()

    if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
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
            visible = pagerState.currentPage == LAST_ON_BOARDING_PAGE,
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
            pagerState.currentPage == LAST_ON_BOARDING_PAGE,
        ) { onFinishClicked() }
    }
}

@Composable
internal fun PagerScreen(
    onBoardingPage: OnBoardingPage,
    context: Context = LocalContext.current,
) {
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
            text = onBoardingPage.title.asString(context),
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
            text = onBoardingPage.description.asString(context),
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
    visible: Boolean = false,
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
            visible = visible,
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
                    text = stringResource(CoreR.string.core_ui_finish),
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
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, device = Devices.AUTOMOTIVE_1024p)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun OnBoardingScreenPreview() {
    val configuration: Configuration = LocalConfiguration.current
    val pagerState = rememberPagerState { ON_BOARDING_PAGE_COUNT }

    LaunchedEffect(Unit) {
        pagerState.animateScrollToPage(page = 3)
    }
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE ->
            WelcomeScreenInLandscape(backgroundColor, pagerState, pages) { }

        else ->
            WelcomeScreenInPortrait(backgroundColor, pagerState, pages) { }
    }
}

val pages =
    listOf(
        OnBoardingPage(
            image = R.drawable.features_on_boarding_first_page_image,
            title = StringResourceId(R.string.features_on_boarding_first_page_title),
            description = StringResourceId(R.string.features_on_boarding_first_page_description),
        ),
        OnBoardingPage(
            image = R.drawable.features_on_boarding_second_page_image,
            title = StringResourceId(R.string.features_on_boarding_second_page_title),
            description = StringResourceId(R.string.features_on_boarding_second_page_description),
        ),
        OnBoardingPage(
            image = R.drawable.features_on_boarding_third_page_image,
            title = StringResourceId(R.string.features_on_boarding_third_page_title),
            description = StringResourceId(R.string.features_on_boarding_third_page_description),
        ),
    )
