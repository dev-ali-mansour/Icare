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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
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

@Composable
fun ClinicianView(
    name: String,
    clinicName: String,
    email: String,
    phone: String,
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
                    fontFamily = helveticaFamily,
                )

                Spacer(modifier = Modifier.height(M_PADDING))

                Text(
                    text = clinicName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = helveticaFamily,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )

                Spacer(modifier = Modifier.height(M_PADDING))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null)
                    Spacer(modifier = Modifier.width(XS_PADDING))
                    Text(text = phone)
                }
                Spacer(modifier = Modifier.height(M_PADDING))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = null)
                    Spacer(modifier = Modifier.width(XS_PADDING))
                    Text(text = email)
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun ClinicianCardPreview() {
    MaterialTheme {
        Column(
            modifier =
                Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(M_PADDING),
        ) {
            ClinicianView(
                name = "Mohammed Ahmed Adel",
                profilePicture = "",
                clinicName = "Clinic 1",
                email = "mAhmed@example.com",
                phone = "0123456789",
                onClick = {},
            )
        }
    }
}
