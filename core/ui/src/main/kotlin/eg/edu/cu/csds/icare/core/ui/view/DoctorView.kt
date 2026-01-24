package eg.edu.cu.csds.icare.core.ui.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.BOARDER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.MAX_SURFACE_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROFILE_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.neumorphicUp
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark

@Composable
fun DoctorView(
    name: String,
    specialty: String,
    availability: String,
    profilePicture: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val context: Context = LocalContext.current
    Box(
        modifier =
            modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(percent = 20),
                ).fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .widthIn(max = MAX_SURFACE_WIDTH)
                .neumorphicUp(
                    shape = RoundedCornerShape(percent = 20),
                    shadowPadding = XS_PADDING,
                ).clickable {
                    onClick()
                },
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
                            .data(data = profilePicture)
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
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = helveticaFamily,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(modifier = Modifier.width(M_PADDING))
                Text(
                    text = specialty,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = helveticaFamily,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
                Spacer(modifier = Modifier.width(M_PADDING))
                Text(
                    text = availability,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = helveticaFamily,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
                )
            }
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
fun DoctorCardPreview() {
    MaterialTheme {
        Column(
            modifier =
                Modifier
                    .padding(XS_PADDING)
                    .background(color = MaterialTheme.colorScheme.background),
        ) {
            DoctorView(
                name = "Dr. John Smith",
                specialty = "Cardiologist",
                availability = "10:00 AM - 6:00 PM",
                profilePicture = "",
                onClick = {},
            )
        }
    }
}
