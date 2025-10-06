package eg.edu.cu.csds.icare.feature.consultation.screen

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.BOARDER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROFILE_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.RECORD_PATIENT_CARD_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.SERVICE_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.U_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.cardBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.trustBlue
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MedicalRecordScreen(
    viewModel: MedicalRecordViewModel = koinViewModel(),
    navigateUp: () -> Unit,
    navigateToConsultationRoute: (Consultation) -> Unit,
) {
    val context: Context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.processEvent(MedicalRecordEvent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is MedicalRecordEffect.NavigateToConsultation -> {
                    navigateToConsultationRoute(effect.consultation)
                }

                is MedicalRecordEffect.ShowError -> {
                    alertMessage = effect.message.asString(context)
                    showAlert = true
                    delay(timeMillis = 3000)
                    showAlert = false
                }
            }
        },
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            stringResource(
                                id = string.core_ui_medical_record,
                            ),
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = barBackgroundColor,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                    ),
                navigationIcon = {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .pullToRefresh(
                        state = refreshState,
                        isRefreshing = uiState.isLoading,
                        onRefresh = {
                            viewModel.processEvent(MedicalRecordEvent.Refresh)
                        },
                    ).padding(paddingValues),
        ) {
            ConstraintLayout(
                modifier =
                    Modifier
                        .background(backgroundColor)
                        .fillMaxWidth(),
            ) {
                val (refresh, line, content) = createRefs()
                Box(
                    modifier =
                        Modifier
                            .constrainAs(line) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }.background(Yellow500)
                            .fillMaxWidth()
                            .height(XS_PADDING),
                )

                MedicalRecordContent(
                    modifier =
                        Modifier.constrainAs(content) {
                            top.linkTo(line.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    uiState = uiState,
                    onEvent = viewModel::processEvent,
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

                if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
            }
        }
    }
}

@Composable
private fun MedicalRecordContent(
    uiState: MedicalRecordState,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onEvent: (MedicalRecordEvent) -> Unit,
) {
    ConstraintLayout(
        modifier =
            modifier
                .fillMaxSize()
                .padding(start = S_PADDING, end = S_PADDING, bottom = S_PADDING)
                .verticalScroll(rememberScrollState()),
    ) {
        val (patientCard, list) = createRefs()

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

            Card(
                modifier =
                    Modifier
                        .constrainAs(patientCard) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }.height(RECORD_PATIENT_CARD_HEIGHT)
                        .padding(XS_PADDING),
                colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
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
                        text = "${
                            stringResource(
                                string.core_ui_name,
                            )
                        }: ${medicalRecord.patientName}",
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
                        color = Color.White,
                        maxLines = 1,
                    )

                    Text(
                        text = "${
                            stringResource(
                                string.core_ui_gender,
                            )
                        }: ${medicalRecord.genderValue}",
                        modifier =
                            Modifier.constrainAs(gender) {
                                top.linkTo(name.bottom)
                                start.linkTo(name.start)
                            },
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        textAlign = TextAlign.Start,
                        fontFamily = helveticaFamily,
                        color = Color.White,
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
                        color = Color.White,
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
                        color = Color.White,
                        maxLines = 1,
                    )

                    Text(
                        text = "${
                            stringResource(
                                string.core_ui_allergies,
                            )
                        }: ${medicalRecord.allergies}",
                        modifier =
                            Modifier.constrainAs(allergies) {
                                top.linkTo(medications.bottom)
                                start.linkTo(name.start)
                            },
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        textAlign = TextAlign.Start,
                        fontFamily = helveticaFamily,
                        color = Color.White,
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
                        color = Color.White,
                        maxLines = 1,
                    )

                    Text(
                        text = "${
                            stringResource(
                                string.core_ui_weight,
                            )
                        }: ${medicalRecord.weight} ${
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
                        color = Color.White,
                        maxLines = 1,
                    )
                }
            }

            LazyColumn(
                modifier =
                    Modifier
                        .constrainAs(list) {
                            top.linkTo(patientCard.bottom, margin = S_PADDING)
                            start.linkTo(patientCard.start)
                            end.linkTo(patientCard.end)
                            bottom.linkTo(parent.bottom, margin = S_PADDING)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
            ) {
                items(medicalRecord.consultations) { consultation ->
                    ConsultationItem(
                        context = context,
                        consultation = consultation,
                        onConsultationClick = {
                            onEvent(MedicalRecordEvent.NavigateToConsultation(it))
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ConsultationItem(
    context: Context,
    consultation: Consultation,
    onConsultationClick: (Consultation) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = S_PADDING)
                .clickable { onConsultationClick(consultation) },
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
                top.linkTo(image.top)
                start.linkTo(image.end, margin = L_PADDING)
                end.linkTo(parent.end, margin = L_PADDING)
                bottom.linkTo(image.bottom)
                width = Dimension.fillToConstraints
            },
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            textAlign = TextAlign.Start,
            fontFamily = helveticaFamily,
            color = trustBlue,
            maxLines = 1,
        )

        Box(
            modifier =
                Modifier
                    .constrainAs(time) {
                        top.linkTo(image.bottom)
                        start.linkTo(name.start)
                    }.background(
                        color = cardBackgroundColor,
                        shape = RoundedCornerShape(XS_PADDING),
                    ).padding(U_PADDING),
        ) {
            Text(
                consultation.dateTime.getFormattedDateTime(context),
                color = Color.White,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
private fun MedicalRecordContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        MedicalRecordContent(
            uiState =
                MedicalRecordState(
                    medicalRecord =
                        MedicalRecord(
                            patientName = "محمد السيد عثمان",
                            patientImage = "https://t.pimg.jp/047/354/467/1/47354467.jpg",
                            gender = 'M',
                            chronicDiseases = "مرض السكري",
                            currentMedications = "أدوية السكري",
                            allergies = "حساسية ضد الفول السوداني",
                            pastSurgeries = "لا يوجد",
                            weight = 80.5,
                            consultations =
                                listOf(
                                    Consultation(
                                        dateTime =
                                            System
                                                .currentTimeMillis()
                                                .minus(other = 1000 * 60 * 60 * 24 * 3),
                                        appointment =
                                            Appointment(
                                                id = 1,
                                                doctorName = "د.سيد عبد الحليم الجوهري",
                                                doctorImage =
                                                    "https://t4.ftcdn.net/jpg/01/98/82/75/360_F_" +
                                                        "198827520_wVNNHdMq4yLJe76WWivQQ5Ev2WtXac4N.webp",
                                            ),
                                    ),
                                ),
                        ),
                ),
            onEvent = {},
        )
    }
}
