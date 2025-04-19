package eg.edu.cu.csds.icare.admin.screen.pharmacy.pharmacist

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
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditPharmacistScreen(
    pharmacyViewModel: PharmacyViewModel,
    onNavigationIconClicked: () -> Unit,
    onProceedButtonClicked: () -> Unit,
    onSuccess: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    val actionResource by pharmacyViewModel.actionResFlow
        .collectAsStateWithLifecycle(initialValue = Resource.Unspecified())
    val pharmaciesResource by pharmacyViewModel.pharmaciesResFlow.collectAsStateWithLifecycle()
    var selectedPharmacist by pharmacyViewModel.selectedPharmacistState
    var pharmaciesExpanded by pharmacyViewModel.pharmaciesExpandedState

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

                selectedPharmacist?.let { pharmacist ->
                    PharmacistDetailsContent(
                        modifier =
                            Modifier.constrainAs(content) {
                                top.linkTo(line.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            },
                        firstName = pharmacist.firstName,
                        lastName = pharmacist.lastName,
                        pharmacyId = pharmacist.pharmacyId,
                        email = pharmacist.email,
                        phone = pharmacist.phone,
                        profilePicture = pharmacist.profilePicture,
                        pharmaciesResource = pharmaciesResource,
                        actionResource = actionResource,
                        pharmaciesExpanded = pharmaciesExpanded,
                        onFirstNameChanged = { selectedPharmacist = pharmacist.copy(firstName = it) },
                        onLastNameChanged = { selectedPharmacist = pharmacist.copy(lastName = it) },
                        onPharmaciesExpandedChange = { pharmaciesExpanded = !pharmaciesExpanded },
                        onPharmaciesDismissRequest = { pharmaciesExpanded = false },
                        onPharmacyClicked = {
                            selectedPharmacist = pharmacist.copy(pharmacyId = it)
                            pharmaciesExpanded = false
                        },
                        onEmailChanged = { selectedPharmacist = pharmacist.copy(email = it) },
                        onPhoneChanged = { selectedPharmacist = pharmacist.copy(phone = it) },
                        onProfilePictureChanged = { selectedPharmacist = pharmacist.copy(profilePicture = it) },
                        onProceedButtonClicked = { onProceedButtonClicked() },
                        onSuccess = { onSuccess() },
                        onError = { onError(it) },
                    )
                }
            }
        }
    }
}
