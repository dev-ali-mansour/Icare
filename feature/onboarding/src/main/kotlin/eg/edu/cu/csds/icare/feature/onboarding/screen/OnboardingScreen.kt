package eg.edu.cu.csds.icare.feature.onboarding.screen

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import eg.edu.cu.csds.icare.core.domain.util.Constants.LAST_ON_BOARDING_PAGE
import eg.edu.cu.csds.icare.core.domain.util.Constants.ON_BOARDING_PAGE_COUNT
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.OnBoardingPage
import eg.edu.cu.csds.icare.core.ui.theme.PAGING_INDICATOR_SPACING
import eg.edu.cu.csds.icare.core.ui.theme.PAGING_INDICATOR_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.activeIndicatorColor
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.descriptionColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.inactiveIndicatorColor
import eg.edu.cu.csds.icare.core.ui.theme.titleColor
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.view.HorizontalPagerIndicator
import eg.edu.cu.csds.icare.core.ui.view.VerticalPagerIndicator
import eg.edu.cu.csds.icare.feature.onboarding.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun OnBoardingScreen(onFinished: () -> Unit) {
    val viewModel: OnboardingViewModel = koinViewModel()
    val configuration: Configuration = LocalConfiguration.current
    val resources = LocalResources.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState { ON_BOARDING_PAGE_COUNT }

    LaunchedEffect(viewModel.effect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is OnBoardingEffect.OnBoardingFinished -> {
                        onFinished()
                    }

                    is OnBoardingEffect.NavigateToRoute -> {}

                    is OnBoardingEffect.ShowError -> {
                        snackbarHostState.showSnackbar(
                            message = effect.message.asString(resources),
                            duration = SnackbarDuration.Short,
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                WelcomeScreenInLandscape(
                    modifier = Modifier.padding(innerPadding),
                    screenBackgroundColor = MaterialTheme.colorScheme.background,
                    pagerState = pagerState,
                    pages = uiState.pages,
                ) {
                    viewModel.handleIntent(OnBoardingIntent.FinishOnBoarding)
                }
            }

            else -> {
                WelcomeScreenInPortrait(
                    modifier = Modifier.padding(innerPadding),
                    screenBackgroundColor = MaterialTheme.colorScheme.background,
                    pagerState = pagerState,
                    pages = uiState.pages,
                ) {
                    viewModel.handleIntent(OnBoardingIntent.FinishOnBoarding)
                }
            }
        }

        if (uiState.isLoading) CircularProgressIndicator()
    }
}

@Composable
private fun WelcomeScreenInPortrait(
    screenBackgroundColor: Color,
    pagerState: PagerState,
    pages: ImmutableList<OnBoardingPage>,
    modifier: Modifier = Modifier,
    onFinishClicked: () -> Unit,
) {
    Column(
        modifier =
            modifier
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
    pages: ImmutableList<OnBoardingPage>,
    modifier: Modifier = Modifier,
    onFinishClicked: () -> Unit,
) {
    Column(
        modifier =
            modifier
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
                    text = stringResource(string.core_ui_finish),
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
        Configuration.ORIENTATION_LANDSCAPE -> {
            WelcomeScreenInLandscape(MaterialTheme.colorScheme.background, pagerState, pages) { }
        }

        else -> {
            WelcomeScreenInPortrait(MaterialTheme.colorScheme.background, pagerState, pages) { }
        }
    }
}

val pages =
    persistentListOf(
        OnBoardingPage(
            image = R.drawable.feature_onboarding_first_page_image,
            title = StringResourceId(R.string.feature_onboarding_first_page_title),
            description = StringResourceId(R.string.feature_onboarding_first_page_description),
        ),
        OnBoardingPage(
            image = R.drawable.feature_onboarding_second_page_image,
            title = StringResourceId(R.string.feature_onboarding_second_page_title),
            description = StringResourceId(R.string.feature_onboarding_second_page_description),
        ),
        OnBoardingPage(
            image = R.drawable.feature_onboarding_first_page_image,
            title = StringResourceId(R.string.feature_onboarding_third_page_title),
            description = StringResourceId(R.string.feature_onboarding_third_page_description),
        ),
    )
