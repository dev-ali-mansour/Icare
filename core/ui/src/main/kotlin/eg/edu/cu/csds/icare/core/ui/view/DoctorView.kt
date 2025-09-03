package eg.edu.cu.csds.icare.core.ui.view

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.BOARDER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROFILE_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.cardBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
fun DoctorView(
    doctor: Doctor,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = M_PADDING, vertical = XS_PADDING),
        colors =
            CardDefaults.cardColors(
                containerColor = cardBackgroundColor,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = XS_PADDING),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(M_PADDING),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier =
                    Modifier
                        .align(Alignment.Top)
                        .padding(XS_PADDING)
                        .clip(CircleShape)
                        .border(BOARDER_SIZE, Color.DarkGray, CircleShape)
                        .size(PROFILE_IMAGE_SIZE),
                painter =
                    rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(context)
                            .data(data = doctor.profilePicture)
                            .placeholder(R.drawable.core_ui_user_placeholder)
                            .error(R.drawable.core_ui_user_placeholder)
                            .build(),
                    ),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.width(M_PADDING))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(XS_PADDING),
            ) {
                Text(
                    text = "${doctor.firstName} ${doctor.lastName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = helveticaFamily,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(modifier = Modifier.width(M_PADDING))
                Text(
                    text = doctor.specialty,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = helveticaFamily,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
                Spacer(modifier = Modifier.width(M_PADDING))
                Text(
                    text = doctor.availability,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = helveticaFamily,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DoctorCardPreview() {
    MaterialTheme {
        Column(
            modifier =
                Modifier
                    .padding(XS_PADDING)
                    .background(color = backgroundColor),
        ) {
            DoctorView(
                doctor =
                    Doctor(
                        id = "101",
                        firstName = "Dr. John",
                        lastName = "Smith",
                        specialty = "Cardiologist",
                        fromTime = System.currentTimeMillis(),
                        toTime = System.currentTimeMillis() + Constants.ONE_DAY,
                        availability = "10:00 AM - 6:00 PM",
                        profilePicture = "",
                        availableSlots = emptyList(),
                        clinicId = 1,
                    ),
                onClick = {},
            )
        }
    }
}
