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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.appointment.R
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.view.SearchTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorsListScreen(
    clinicDoctorsViewModel: ClinicViewModel,
    onDoctorClick: (Doctor) -> Unit,
    onBack: () -> Unit,
) {
    val actionResource by clinicDoctorsViewModel.actionResFlow
        .collectAsStateWithLifecycle(initialValue = Resource.Unspecified())

    val doctorsResource by clinicDoctorsViewModel.doctorsResFlow.collectAsStateWithLifecycle()
    var searchQuery: String by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        clinicDoctorsViewModel.listDoctors()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.doctors_list_title)) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = barBackgroundColor,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                    ),
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
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
        ConstraintLayout(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(backgroundColor),
        ) {
            val (line, searchBar, content) = createRefs()

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

            SearchTextField(
                modifier =
                    Modifier
                        .constrainAs(searchBar) {
                            top.linkTo(line.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }.fillMaxWidth()
                        .padding(horizontal = M_PADDING, vertical = S_PADDING),
                placeholder = stringResource(R.string.search),
                value = searchQuery,
                focus = false,
                onValueChange = { searchQuery = it },
                onClear = { searchQuery = "" },
                onSearch = {},
            )

            DoctorsListContent(
                modifier =
                    Modifier.constrainAs(content) {
                        top.linkTo(searchBar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                doctorsRes = doctorsResource,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onDoctorClick = onDoctorClick,
            )
        }
    }
}
