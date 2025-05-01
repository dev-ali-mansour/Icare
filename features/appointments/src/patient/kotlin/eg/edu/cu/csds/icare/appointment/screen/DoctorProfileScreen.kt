package eg.edu.cu.csds.icare.appointment.screen

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
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.appointment.AppointmentViewModel
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.view.SuccessesDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DoctorProfileScreen(
    mainViewModel: MainViewModel,
    clinicViewModel: ClinicViewModel,
    appointmentViewModel: AppointmentViewModel,
    onNavigationIconClicked: () -> Unit,
    onProceedButtonClicked: (String, String) -> Unit,
    onSuccess: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    val userRes by mainViewModel.currentUserFlow.collectAsStateWithLifecycle()
    val actionResource by appointmentViewModel.actionResFlow
        .collectAsStateWithLifecycle(initialValue = Resource.Unspecified())
    var selectedDoctor by clinicViewModel.selectedDoctorState
    val doctorScheduleResource by clinicViewModel.doctorScheduleResFlow.collectAsStateWithLifecycle()
    var selectedSlot by appointmentViewModel.selectedSlotState
    var showSuccessDialog by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.doctor_profile)) },
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
                        selectedDoctor?.let { doctor ->
                            clinicViewModel.getDoctorSchedule(doctor.id)
                        }
                    })
                    .padding(paddingValues),
        ) {
            ConstraintLayout(
                modifier =
                    Modifier
                        .background(backgroundColor)
                        .fillMaxWidth(),
            ) {
                val (line, content, refresh) = createRefs()
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
                DoctorProfileContent(
                    modifier =
                        Modifier.constrainAs(content) {
                            top.linkTo(line.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    userRes = userRes,
                    selectedDoctor = selectedDoctor,
                    doctorScheduleRes = doctorScheduleResource,
                    actionResource = actionResource,
                    showLoading = { isRefreshing = it },
                    selectedSlot = selectedSlot,
                    onSlotSelected = { selectedSlot = it },
                    onProceedButtonClicked = { doctorId, userId ->
                        onProceedButtonClicked(doctorId, userId)
                    },
                    onSuccess = {
                        scope.launch {
                            showSuccessDialog = true
                            delay(timeMillis = 2000)
                            showSuccessDialog = false
                            onSuccess()
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
