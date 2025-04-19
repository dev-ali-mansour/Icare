package eg.edu.cu.csds.icare.admin.screen.clinic.staff

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditClinicStaffScreen(
    clinicViewModel: ClinicViewModel,
    onNavigationIconClicked: () -> Unit,
    onProceedButtonClicked: () -> Unit,
    onSuccess: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    val actionResource by clinicViewModel.actionResFlow
        .collectAsStateWithLifecycle(initialValue = Resource.Unspecified())
    val clinicsResource by clinicViewModel.clinicsResFlow.collectAsStateWithLifecycle()
    var selectedDoctor by clinicViewModel.selectedDoctorState
    var clinicsExpanded by clinicViewModel.clinicsExpandedState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.edit_clinic_staff)) },
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
                    .padding(paddingValues),
        ) {
            ConstraintLayout(
                modifier =
                    Modifier
                        .background(backgroundColor)
                        .fillMaxWidth(),
            ) {
                val (line, content) = createRefs()
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

                selectedDoctor?.let { doctor ->
                    ClinicStaffDetailsContent(
                        modifier =
                            Modifier.constrainAs(content) {
                                top.linkTo(line.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            },
                        firstName = doctor.firstName,
                        lastName = doctor.lastName,
                        clinicId = doctor.clinicId,
                        email = doctor.email,
                        phone = doctor.phone,
                        profilePicture = doctor.profilePicture,
                        clinicsResource = clinicsResource,
                        actionResource = actionResource,
                        clinicsExpanded = clinicsExpanded,
                        onFirstNameChanged = { selectedDoctor = doctor.copy(firstName = it) },
                        onLastNameChanged = { selectedDoctor = doctor.copy(lastName = it) },
                        onClinicsExpandedChange = { clinicsExpanded = !clinicsExpanded },
                        onClinicsDismissRequest = { clinicsExpanded = false },
                        onClinicClicked = {
                            selectedDoctor = doctor.copy(clinicId = it)
                            clinicsExpanded = false
                        },
                        onEmailChanged = { selectedDoctor = doctor.copy(email = it) },
                        onPhoneChanged = { selectedDoctor = doctor.copy(phone = it) },
                        onProfilePictureChanged = { selectedDoctor = doctor.copy(profilePicture = it) },
                        onProceedButtonClicked = { onProceedButtonClicked() },
                        onSuccess = { onSuccess },
                        onError = { onError(it) },
                    )
                }
            }
        }
    }
}
