package eg.edu.cu.csds.icare.admin.screen.center.staff

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
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
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
internal fun NewCenterStaffScreen(
    centerViewModel: CenterViewModel,
    onNavigationIconClicked: () -> Unit,
    onProceedButtonClicked: () -> Unit,
    onSuccess: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
    context: Context = LocalContext.current,
) {
    val actionResource by centerViewModel.actionResFlow
        .collectAsStateWithLifecycle(initialValue = Resource.Unspecified())
    val centersResource by centerViewModel.centersResFlow.collectAsStateWithLifecycle()
    var firstName by centerViewModel.firstNameState
    var lastName by centerViewModel.lastNameState
    var centerId by centerViewModel.centerIdState
    var email by centerViewModel.emailState
    var phone by centerViewModel.phoneState
    var centersExpanded by centerViewModel.centersExpandedState
    var showSuccessDialog by centerViewModel.showSuccessDialog
    var isRefreshing by centerViewModel.isRefreshing
    val state = rememberPullToRefreshState()
    val scope: CoroutineScope = rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        centerViewModel.resetStates()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.new_center_staff)) },
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

                CenterStaffDetailsContent(
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
                    clinicId = centerId,
                    email = email,
                    phone = phone,
                    centersResource = centersResource,
                    actionResource = actionResource,
                    centersExpanded = centersExpanded,
                    showLoading = { isRefreshing = it },
                    onFirstNameChanged = { firstName = it },
                    onLastNameChanged = { lastName = it },
                    onCentersExpandedChange = { centersExpanded = !centersExpanded },
                    onCentersDismissRequest = { centersExpanded = false },
                    onCenterClicked = {
                        centerId = it
                        centersExpanded = false
                    },
                    onEmailChanged = { email = it },
                    onPhoneChanged = { phone = it },
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

                                centerId == 0.toLong() -> {
                                    alertMessage = context.getString(R.string.center_error)
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
