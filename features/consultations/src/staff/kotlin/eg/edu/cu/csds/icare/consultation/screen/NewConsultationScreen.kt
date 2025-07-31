package eg.edu.cu.csds.icare.consultation.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.consultation.ConsultationViewModel
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.core.ui.view.SuccessesDialog
import eg.edu.cu.csds.icare.core.data.util.getFormattedDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewConsultationScreen(
    pharmacyViewModel: PharmacyViewModel,
    centerViewModel: CenterViewModel,
    consultationViewModel: ConsultationViewModel,
    onNavigationIconClicked: () -> Unit,
    onPatientCardClick: (String) -> Unit,
    onProceedButtonClicked: () -> Unit,
    navigateToScreen: (Screen) -> Unit,
    onError: suspend (Throwable?) -> Unit,
    context: Context = LocalContext.current,
) {
    val pharmaciesRes by pharmacyViewModel.pharmaciesResFlow.collectAsStateWithLifecycle()
    val centersRes by centerViewModel.centersResFlow.collectAsStateWithLifecycle()
    val actionResource by consultationViewModel.actionResFlow
        .collectAsStateWithLifecycle(initialValue = Resource.Unspecified())
    var pharmaciesExpanded by pharmacyViewModel.pharmaciesExpandedState
    var labCentersExpanded by consultationViewModel.labCentersExpandedState
    var imagingCentersExpanded by consultationViewModel.imagingCentersExpandedState
    var appointment by consultationViewModel.appointmentState
    var diagnosis by consultationViewModel.diagnosisState
    var pharmacyId by consultationViewModel.pharmacyIdState
    var medications by consultationViewModel.medicationsState
    var labCenterId by consultationViewModel.labCenterIdState
    var labTests by consultationViewModel.labTestsState
    var imagingCenterId by consultationViewModel.imagingCenterIdState
    var imagingTests by consultationViewModel.imagingTestsState
    var followUpdDate by consultationViewModel.followUpdDateState
    var showSuccessDialog by consultationViewModel.showSuccessDialog
    var isRefreshing by consultationViewModel.isRefreshing
    val state = rememberPullToRefreshState()
    val scope: CoroutineScope = rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.new_consultation)) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = barBackgroundColor,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                    ),
                navigationIcon = {
                    IconButton(onClick = { onNavigationIconClicked() }) {
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
                    .pullToRefresh(state = state, isRefreshing = isRefreshing, onRefresh = {
                        pharmacyViewModel.listPharmacies(forceRefresh = true)
                        centerViewModel.listCenters(forceRefresh = true)
                    })
                    .padding(paddingValues),
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

                ConsultationDetailsContent(
                    modifier =
                        Modifier.constrainAs(content) {
                            top.linkTo(line.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    pharmaciesRes = pharmaciesRes,
                    centersRes = centersRes,
                    readOnly = false,
                    dateTime = System.currentTimeMillis(),
                    appointment = appointment,
                    diagnosis = diagnosis,
                    pharmacyId = pharmacyId,
                    pharmaciesExpanded = pharmaciesExpanded,
                    medications = medications,
                    prescriptionStatusId = 1,
                    labCenterId = labCenterId,
                    labCentersExpanded = labCentersExpanded,
                    labTests = labTests,
                    labTestStatusId = 1,
                    imagingCenterId = imagingCenterId,
                    imagingCentersExpanded = imagingCentersExpanded,
                    imagingTests = imagingTests,
                    imgTestStatusId = 1,
                    followUpdDate = followUpdDate,
                    actionResource = actionResource,
                    showLoading = { isRefreshing = it },
                    onPatientCardClick = { onPatientCardClick(it) },
                    onDiagnosisChanged = { diagnosis = it },
                    onMedicationsChanged = { medications = it },
                    onLabTestsChanged = { labTests = it },
                    onImagingTestsChanged = { imagingTests = it },
                    onPharmaciesExpandedChange = { pharmaciesExpanded = !pharmaciesExpanded },
                    onPharmaciesDismissRequest = { pharmaciesExpanded = false },
                    onPharmacyClicked = {
                        pharmacyId = it
                        pharmaciesExpanded = false
                    },
                    onLabCentersExpandedChange = { labCentersExpanded = !labCentersExpanded },
                    onLabCentersDismissRequest = { labCentersExpanded = false },
                    onLabCenterClicked = {
                        labCenterId = it
                        labCentersExpanded = false
                    },
                    onImagingCentersExpandedChange = {
                        imagingCentersExpanded = !imagingCentersExpanded
                    },
                    onImagingCentersDismissRequest = { imagingCentersExpanded = false },
                    onImagingCenterClicked = {
                        imagingCenterId = it
                        imagingCentersExpanded = false
                    },
                    onFollowUpDateChanged = { followUpdDate = it },
                    onProceedButtonClicked = {
                        scope.launch {
                            when {
                                diagnosis.trim().isEmpty() -> {
                                    alertMessage = context.getString(R.string.diagnosis_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                pharmacyId == 0.toLong() && medications.trim().isNotEmpty() -> {
                                    alertMessage = context.getString(R.string.pharmacy_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                pharmacyId > 0.toLong() && medications.trim().isEmpty() -> {
                                    alertMessage = context.getString(R.string.medications_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                labCenterId == 0.toLong() && labTests.trim().isNotEmpty() -> {
                                    alertMessage = context.getString(R.string.lab_center_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                labCenterId > 0.toLong() && labTests.trim().isEmpty() -> {
                                    alertMessage = context.getString(R.string.lab_tests_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                imagingCenterId == 0.toLong() &&
                                    imagingTests.trim().isNotEmpty() -> {
                                    alertMessage = context.getString(R.string.imaging_center_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                imagingCenterId > 0.toLong() &&
                                    imagingTests.trim().isEmpty() -> {
                                    alertMessage = context.getString(R.string.imaging_tests_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                followUpdDate.getFormattedDate(context) ==
                                    0L.getFormattedDate(context) -> {
                                    alertMessage = context.getString(R.string.follow_up_date_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                else -> onProceedButtonClicked()
                            }
                        }
                    },
                    onSuccess = {
                        scope.launch {
                            showSuccessDialog = true
                            delay(timeMillis = 2000)
                            showSuccessDialog = false
                            delay(timeMillis = 1000)
                            navigateToScreen(Screen.Home)
                        }
                    },
                    onError = { onError(it) },
                )

                Indicator(
                    modifier =
                        Modifier.constrainAs(refresh) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    isRefreshing = isRefreshing,
                    state = state,
                )
                if (showSuccessDialog) SuccessesDialog {}
                if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
            }
        }
    }
}
