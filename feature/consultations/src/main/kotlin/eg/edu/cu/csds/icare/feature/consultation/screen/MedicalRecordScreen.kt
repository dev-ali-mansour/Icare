package eg.edu.cu.csds.icare.feature.consultation.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.core.data.util.getFormattedDateTime
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.CommonTopAppBar
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.BOARDER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.MAX_SURFACE_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROFILE_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.RECORD_PATIENT_CARD_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.SERVICE_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.U_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.calculateGridColumns
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MedicalRecordScreen(
    onNavigationIconClicked: () -> Unit,
    navigateToConsultationRoute: (Consultation) -> Unit,
) {
    val viewModel: MedicalRecordViewModel = koinViewModel()
    val context: Context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(MedicalRecordIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is MedicalRecordEffect.NavigateToConsultation -> {
                    navigateToConsultationRoute(effect.consultation)
                }

                is MedicalRecordEffect.ShowError -> {
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
            CommonTopAppBar(title = stringResource(id = string.core_ui_medical_record)) {
                onNavigationIconClicked()
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        ConstraintLayout(
            modifier =
                Modifier
                    .fillMaxSize()
                    .pullToRefresh(
                        state = refreshState,
                        isRefreshing = uiState.isLoading,
                        onRefresh = {
                            viewModel.handleIntent(MedicalRecordIntent.Refresh)
                        },
                    ).padding(innerPadding),
        ) {
            val (refresh, content) = createRefs()

            MedicalRecordContent(
                modifier =
                    Modifier.constrainAs(content) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                uiState = uiState,
                onIntent = viewModel::handleIntent,
            )

            Indicator(
                modifier =
                    Modifier.constrainAs(refresh) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                isRefreshing = uiState.isLoading,
                state = refreshState,
            )
        }
    }
}

@Composable
private fun MedicalRecordContent(
    uiState: MedicalRecordState,
    modifier: Modifier = Modifier,
    onIntent: (MedicalRecordIntent) -> Unit,
) {
    val context: Context = LocalContext.current
    uiState.medicalRecord?.let { record ->
        val medicalRecord =
            record.copy(
                genderValue =
                    if (record.gender == 'M') {
                        stringResource(string.core_ui_male)
                    } else {
                        stringResource(
                            string.core_ui_female,
                        )
                    },
            )

        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(calculateGridColumns()),
            state = rememberLazyGridState(),
            contentPadding = PaddingValues(all = S_PADDING),
            horizontalArrangement = Arrangement.spacedBy(S_PADDING),
            verticalArrangement = Arrangement.spacedBy(S_PADDING),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .widthIn(max = MAX_SURFACE_WIDTH) // Added to match ConsultationItem
                            .height(RECORD_PATIENT_CARD_HEIGHT),
                    elevation = CardDefaults.cardElevation(XS_PADDING),
                ) {
                    ConstraintLayout(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(M_PADDING),
                    ) {
                        val (image, name, gender, diseases, medications, allergies, surgeries, weight) =
                            createRefs()

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
                                        end.linkTo(parent.end)
                                    },
                            painter =
                                rememberAsyncImagePainter(
                                    ImageRequest
                                        .Builder(context)
                                        .data(data = medicalRecord.patientImage)
                                        .placeholder(
                                            R.drawable.core_ui_user_placeholder,
                                        ).error(R.drawable.core_ui_user_placeholder)
                                        .build(),
                                ),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                        )

                        Text(
                            text = "${stringResource(string.core_ui_name)}: ${medicalRecord.patientName}",
                            modifier =
                                Modifier.constrainAs(name) {
                                    top.linkTo(image.bottom, margin = S_PADDING)
                                    start.linkTo(parent.start)
                                    width = Dimension.fillToConstraints
                                },
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            textAlign = TextAlign.Start,
                            fontFamily = helveticaFamily,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                        )

                        Text(
                            text = "${stringResource(string.core_ui_gender)}: ${medicalRecord.genderValue}",
                            modifier =
                                Modifier.constrainAs(gender) {
                                    top.linkTo(name.bottom)
                                    start.linkTo(name.start)
                                },
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            textAlign = TextAlign.Start,
                            fontFamily = helveticaFamily,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                        )

                        Text(
                            text = "${
                                stringResource(
                                    string.core_ui_chronic_diseases,
                                )
                            }: ${medicalRecord.chronicDiseases}",
                            modifier =
                                Modifier.constrainAs(diseases) {
                                    top.linkTo(gender.bottom)
                                    start.linkTo(name.start)
                                },
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            textAlign = TextAlign.Start,
                            fontFamily = helveticaFamily,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                        )

                        Text(
                            text = "${
                                stringResource(
                                    string.core_ui_current_medications,
                                )
                            }: ${medicalRecord.currentMedications}",
                            modifier =
                                Modifier.constrainAs(medications) {
                                    top.linkTo(diseases.bottom)
                                    start.linkTo(name.start)
                                },
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            textAlign = TextAlign.Start,
                            fontFamily = helveticaFamily,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                        )

                        Text(
                            text = "${stringResource(string.core_ui_allergies)}: ${medicalRecord.allergies}",
                            modifier =
                                Modifier.constrainAs(allergies) {
                                    top.linkTo(medications.bottom)
                                    start.linkTo(name.start)
                                },
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            textAlign = TextAlign.Start,
                            fontFamily = helveticaFamily,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                        )

                        Text(
                            text = "${
                                stringResource(
                                    string.core_ui_past_surgeries,
                                )
                            }: ${medicalRecord.pastSurgeries}",
                            modifier =
                                Modifier.constrainAs(surgeries) {
                                    top.linkTo(allergies.bottom)
                                    start.linkTo(name.start)
                                },
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            textAlign = TextAlign.Start,
                            fontFamily = helveticaFamily,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                        )

                        Text(
                            text = "${stringResource(string.core_ui_weight)}: ${medicalRecord.weight} ${
                                stringResource(
                                    string.core_ui_kgm,
                                )
                            }",
                            modifier =
                                Modifier.constrainAs(weight) {
                                    top.linkTo(surgeries.bottom)
                                    start.linkTo(name.start)
                                },
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            textAlign = TextAlign.Start,
                            fontFamily = helveticaFamily,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                        )
                    }
                }
            }

            items(
                items = medicalRecord.consultations,
                key = { consultation -> consultation.id },
                span = { GridItemSpan(1) },
            ) { consultation ->
                ConsultationItem(
                    consultation = consultation,
                    onConsultationClick = {
                        onIntent(MedicalRecordIntent.NavigateToConsultation(it))
                    },
                )
            }
        }
    }
}

@Composable
private fun ConsultationItem(
    consultation: Consultation,
    onConsultationClick: (Consultation) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context: Context = LocalContext.current
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .widthIn(max = MAX_SURFACE_WIDTH)
                .clickable { onConsultationClick(consultation) },
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
            ),
        elevation = CardDefaults.cardElevation(XS_PADDING),
    ) {
        ConstraintLayout(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(M_PADDING),
        ) {
            val (image, name, time) = createRefs()
            Image(
                modifier =
                    Modifier
                        .padding(XS_PADDING)
                        .clip(CircleShape)
                        .border(BOARDER_SIZE, Color.DarkGray, CircleShape)
                        .size(SERVICE_ICON_SIZE)
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                painter =
                    rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(context)
                            .data(data = consultation.appointment.doctorImage)
                            .placeholder(R.drawable.core_ui_user_placeholder)
                            .error(R.drawable.core_ui_user_placeholder)
                            .build(),
                    ),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )

            Text(
                consultation.appointment.doctorName,
                modifier.constrainAs(name) {
                    top.linkTo(parent.top)
                    start.linkTo(image.end, margin = L_PADDING)
                    end.linkTo(parent.end, margin = L_PADDING)
                    bottom.linkTo(image.bottom)
                    width = Dimension.fillToConstraints
                },
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                textAlign = TextAlign.Start,
                fontFamily = helveticaFamily,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )

            Text(
                modifier =
                    Modifier
                        .constrainAs(time) {
                            top.linkTo(name.bottom)
                            start.linkTo(name.start)
                            bottom.linkTo(parent.bottom, margin = M_PADDING)
                        }.background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(XS_PADDING),
                        ).padding(U_PADDING),
                text = consultation.dateTime.getFormattedDateTime(context),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
            )
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
private fun MedicalRecordContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            MedicalRecordContent(
                uiState =
                    MedicalRecordState(
                        medicalRecord =
                            MedicalRecord(
                                patientName = "Mohamed El-sayed Othman",
                                gender = 'M',
                                chronicDiseases = "Diabetes",
                                currentMedications = "Diabetes medications",
                                allergies = "Peanut Allergy",
                                pastSurgeries = "None",
                                weight = 80.5,
                                consultations =
                                    listOf(
                                        Consultation(
                                            id = 1,
                                            dateTime = System.currentTimeMillis().minus(other = 3600000),
                                            appointment = Appointment(doctorName = "Dr. Ahmed Hussein"),
                                        ),
                                        Consultation(
                                            id = 2,
                                            dateTime = System.currentTimeMillis().minus(other = 7200000),
                                            appointment = Appointment(doctorName = "Dr. Akram Mohamed"),
                                        ),
                                        Consultation(
                                            id = 3,
                                            dateTime = System.currentTimeMillis().minus(other = 10800000),
                                            appointment = Appointment(doctorName = "Dr. Salam Hussein"),
                                        ),
                                        Consultation(
                                            id = 4,
                                            dateTime = System.currentTimeMillis().minus(other = 21600000),
                                            appointment = Appointment(doctorName = "Dr. Shaker Mohamed"),
                                        ),
                                        Consultation(
                                            id = 5,
                                            dateTime = System.currentTimeMillis().minus(other = 259200000),
                                            appointment = Appointment(doctorName = "Dr. Yousef Salem"),
                                        ),
                                    ),
                            ),
                    ),
                onIntent = {},
            )
        }
    }
}
