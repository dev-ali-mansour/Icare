package eg.edu.cu.csds.icare.appointment.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import eg.edu.cu.csds.icare.appointment.R
import eg.edu.cu.csds.icare.appointment.components.DoctorCard
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.view.SearchTextField

@Composable
fun DoctorsListContent(
    doctorsRes: Resource<List<Doctor>>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onDoctorClick: (Doctor) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (doctorsRes) {
            is Resource.Unspecified -> {}
            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .padding(XL_PADDING),
                )
            }

            is Resource.Success -> {
                val doctors =
                    doctorsRes.data
                        .orEmpty()
                        .filter { doctor ->
                            searchQuery.isEmpty() ||
                                doctor.name.contains(searchQuery, ignoreCase = true) ||
                                doctor.specialty.contains(searchQuery, ignoreCase = true)
                        }

                if (doctors.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.no_doctors_available),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(M_PADDING),
                        verticalArrangement = Arrangement.spacedBy(S_PADDING),
                    ) {
                        items(doctors) { doctor ->
                            DoctorCard(
                                doctor = doctor,
                                onClick = { onDoctorClick(doctor) },
                            )
                        }
                    }
                }
            }

            is Resource.Error -> {
                Text(
                    text = stringResource(id = R.string.error_loading_doctors),
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
fun DoctorsListContentPreview() {
    val doctors =
        listOf(
            Doctor(
                id = "1",
                firstName = "Dr. Anna ",
                lastName = "Jones",
                specialty = "General Practitioner",
            ),
            Doctor(
                id = "2",
                firstName = "Dr. Tiya ",
                lastName = "Mcdariel",
                specialty = "Heart Specialist",
            ),
            Doctor(
                id = "3",
                firstName = "Dr. John ",
                lastName = "Berry",
                specialty = "General Practitioner",
            ),
        )

    Column(modifier = Modifier.background(backgroundColor)) {
        SearchTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = M_PADDING, vertical = S_PADDING),
            placeholder = "Search",
            value = "",
            focus = false,
            onValueChange = {},
            onClear = {},
            onSearch = {},
        )

        DoctorsListContent(
            doctorsRes = Resource.Success(doctors),
            searchQuery = "",
            onDoctorClick = {},
            onSearchQueryChange = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
