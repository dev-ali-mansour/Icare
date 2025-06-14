package eg.edu.cu.csds.icare.admin.screen.clinic.doctor

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
import androidx.compose.runtime.LaunchedEffect
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
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.core.ui.view.SuccessesDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewDoctorScreen(
    clinicViewModel: ClinicViewModel,
    onNavigationIconClicked: () -> Unit,
    onProceedButtonClicked: () -> Unit,
    onSuccess: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
    context: Context = LocalContext.current,
) {
    val actionResource by clinicViewModel.actionResFlow
        .collectAsStateWithLifecycle(initialValue = Resource.Unspecified())
    val clinicsResource by clinicViewModel.clinicsResFlow.collectAsStateWithLifecycle()
    var firstName by clinicViewModel.firstNameState
    var lastName by clinicViewModel.lastNameState
    var clinicId by clinicViewModel.clinicIdState
    var email by clinicViewModel.emailState
    var phone by clinicViewModel.phoneState
    var specialty by clinicViewModel.specialityState
    var fromTime by clinicViewModel.fromTimeState
    var toTime by clinicViewModel.toTimeState
    var price by clinicViewModel.priceState
    var rating by clinicViewModel.ratingState
    var clinicsExpanded by clinicViewModel.clinicsExpandedState
    var showSuccessDialog by clinicViewModel.showSuccessDialog
    var isRefreshing by clinicViewModel.isRefreshing
    val state = rememberPullToRefreshState()
    val scope: CoroutineScope = rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        clinicViewModel.resetStates()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.new_doctor)) },
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
                        clinicViewModel.listDoctors(forceRefresh = true)
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

                DoctorDetailsContent(
                    modifier =
                        Modifier.constrainAs(content) {
                            top.linkTo(line.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    firstName = firstName,
                    lastName = lastName,
                    clinicId = clinicId,
                    email = email,
                    phone = phone,
                    speciality = specialty,
                    fromTime = fromTime,
                    toTime = toTime,
                    price = price,
                    rating = rating,
                    clinicsResource = clinicsResource,
                    actionResource = actionResource,
                    showLoading = { isRefreshing = it },
                    clinicsExpanded = clinicsExpanded,
                    onFirstNameChanged = { firstName = it },
                    onLastNameChanged = { lastName = it },
                    onClinicsExpandedChange = { clinicsExpanded = !clinicsExpanded },
                    onClinicsDismissRequest = { clinicsExpanded = false },
                    onClinicClicked = {
                        clinicId = it
                        clinicsExpanded = false
                    },
                    onEmailChanged = { email = it },
                    onPhoneChanged = { phone = it },
                    onSpecialityChanged = { specialty = it },
                    onFromTimeChanged = { fromTime = it },
                    onToTimeChanged = { toTime = it },
                    onPriceChanged = { price = it },
                    onRatingChanged = { rating = it },
                    onProceedButtonClicked = {
                        scope.launch {
                            when {
                                firstName.isBlank() -> {
                                    alertMessage = context.getString(R.string.first_name_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                lastName.isBlank() -> {
                                    alertMessage = context.getString(R.string.last_name_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                clinicId == 0.toLong() -> {
                                    alertMessage = context.getString(R.string.clinic_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                !email.isValidEmail -> {
                                    alertMessage = context.getString(R.string.email_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                phone.isBlank() || phone.length < Constants.PHONE_LENGTH -> {
                                    alertMessage = context.getString(R.string.phone_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                specialty.isBlank() -> {
                                    alertMessage = context.getString(R.string.speciality_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                price < 1.0 -> {
                                    alertMessage = context.getString(R.string.service_price_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                rating <= 0.0 -> {
                                    alertMessage = context.getString(R.string.rating_error)
                                    showAlert = true
                                    delay(timeMillis = 3000)
                                    showAlert = false
                                }

                                fromTime == toTime -> {
                                    alertMessage = context.getString(R.string.doctor_times_error)
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
                if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
            }
        }
    }
}
