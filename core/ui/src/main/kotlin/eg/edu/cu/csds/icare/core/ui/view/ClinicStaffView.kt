package eg.edu.cu.csds.icare.core.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
fun ClinicStaffView(
    clinicStaff: ClinicStaff,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = M_PADDING, vertical = XS_PADDING),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(M_PADDING),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = clinicStaff.profilePicture,
                contentDescription = "",
                modifier =
                    Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .align(Alignment.Top),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "${clinicStaff.firstName} ${clinicStaff.lastName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = helveticaFamily,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(modifier = Modifier.height(M_PADDING))
                Text(
                    text = clinicStaff.clinicId.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = helveticaFamily,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
                Spacer(modifier = Modifier.height(M_PADDING))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null)
                    Spacer(modifier = Modifier.width(XS_PADDING))
                    Text(text = clinicStaff.phone)
                }
                Spacer(modifier = Modifier.height(M_PADDING))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = null)
                    Spacer(modifier = Modifier.width(XS_PADDING))
                    Text(text = clinicStaff.email)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ClinicStaffCardPreview() {
    MaterialTheme {
        Column(
            modifier =
                Modifier
                    .padding(XS_PADDING)
                    .background(color = backgroundColor),
        ) {
            ClinicStaffView(
                clinicStaff =
                    ClinicStaff(
                        id = "101",
                        firstName = "Mohammed Adel",
                        lastName = "Ahmed",
                        profilePicture = "",
                        clinicId = 1,
                        email = "mAhmed@example.com",
                        phone = "0123456789",
                    ),
                onClick = {},
            )
        }
    }
}
