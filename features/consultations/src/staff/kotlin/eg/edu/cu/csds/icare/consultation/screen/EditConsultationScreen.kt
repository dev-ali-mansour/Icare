package eg.edu.cu.csds.icare.consultation.screen

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import eg.edu.cu.csds.icare.core.ui.view.SuccessesDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditConsultationScreen(
    pharmacyViewModel: PharmacyViewModel,
    centerViewModel: CenterViewModel,
    consultationViewModel: ConsultationViewModel,
    onNavigationIconClicked: () -> Unit,
    onPatientCardClick: (String) -> Unit,
    onProceedButtonClicked: () -> Unit,
    navigateToScreen: (Screen) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    val pharmaciesRes by pharmacyViewModel.pharmaciesResFlow.collectAsStateWithLifecycle()
    val centersRes by centerViewModel.centersResFlow.collectAsStateWithLifecycle()
    val actionResource by consultationViewModel.actionResFlow
        .collectAsStateWithLifecycle(initialValue = Resource.Unspecified())
    var selectedConsultation by consultationViewModel.selectedConsultationState
    var pharmaciesExpanded by pharmacyViewModel.pharmaciesExpandedState
    var labCentersExpanded by consultationViewModel.labCentersExpandedState
    var imagingCentersExpanded by consultationViewModel.imagingCentersExpandedState
    var showSuccessDialog by consultationViewModel.showSuccessDialog
    var isRefreshing by consultationViewModel.isRefreshing
    val state = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.edit_consultation)) },
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

                selectedConsultation?.let { consultation ->
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
                        readOnly = consultation.readOnly,
                        dateTime = consultation.dateTime,
                        appointment = consultation.appointment,
                        diagnosis = consultation.diagnosis,
                        pharmacyId = consultation.pharmacyId,
                        pharmaciesExpanded = pharmaciesExpanded,
                        medications = consultation.medications,
                        prescriptionStatusId = consultation.prescriptionStatusId,
                        labCenterId = consultation.labCenterId,
                        labCentersExpanded = labCentersExpanded,
                        labTests = consultation.labTests,
                        labTestStatusId = consultation.labTestStatusId,
                        imagingCenterId = consultation.imagingCenterId,
                        imagingCentersExpanded = imagingCentersExpanded,
                        imagingTests = consultation.imagingTests,
                        imgTestStatusId = consultation.imgTestStatusId,
                        followUpdDate = consultation.followUpdDate,
                        actionResource = actionResource,
                        showLoading = { isRefreshing = it },
                        onPatientCardClick = { onPatientCardClick(it) },
                        onDiagnosisChanged = { consultation.copy(diagnosis = it) },
                        onMedicationsChanged = { consultation.copy(medications = it) },
                        onLabTestsChanged = { consultation.copy(labTests = it) },
                        onImagingTestsChanged = { consultation.copy(imagingTests = it) },
                        onPharmaciesExpandedChange = { pharmaciesExpanded = !pharmaciesExpanded },
                        onPharmaciesDismissRequest = { pharmaciesExpanded = false },
                        onPharmacyClicked = {
                            consultation.copy(pharmacyId = it)
                            pharmaciesExpanded = false
                        },
                        onLabCentersExpandedChange = { labCentersExpanded = !labCentersExpanded },
                        onLabCentersDismissRequest = { labCentersExpanded = false },
                        onLabCenterClicked = {
                            consultation.copy(labCenterId = it)
                            labCentersExpanded = false
                        },
                        onImagingCentersExpandedChange = {
                            imagingCentersExpanded = !imagingCentersExpanded
                        },
                        onImagingCentersDismissRequest = { imagingCentersExpanded = false },
                        onImagingCenterClicked = {
                            consultation.copy(imagingCenterId = it)
                            imagingCentersExpanded = false
                        },
                        onFollowUpDateChanged = { consultation.copy(followUpdDate = it) },
                        onProceedButtonClicked = { onProceedButtonClicked() },
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
                }
            }
        }
    }
}
