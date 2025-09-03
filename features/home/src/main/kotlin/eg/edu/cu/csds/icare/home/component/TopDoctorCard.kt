package eg.edu.cu.csds.icare.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.ui.theme.DOCTOR_CARD_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROMOTION_ITEM_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow300
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.home.R

@Composable
fun TopDoctorCard(
    doctor: Doctor,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .padding(end = S_PADDING)
                .width(DOCTOR_CARD_WIDTH),
    ) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(doctor.profilePicture)
                    .crossfade(true)
                    .build(),
            placeholder = painterResource(eg.edu.cu.csds.icare.core.ui.R.drawable.core_ui_placeholder),
            contentDescription = null,
            modifier =
                Modifier
                    .height(PROMOTION_ITEM_HEIGHT)
                    .fillMaxWidth(),
            error = painterResource(R.drawable.features_home_doctor_placeholder),
            contentScale = ContentScale.FillBounds,
        )
        Text(
            text = doctor.name,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            fontFamily = helveticaFamily,
            color = textColor,
            maxLines = 1,
        )
        Text(
            text = doctor.specialty,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontFamily = helveticaFamily,
            color = textColor,
            maxLines = 1,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = Yellow300,
                modifier = Modifier.size(M_PADDING),
            )
            Text(
                text = "${doctor.rating}",
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
            )
        }
    }
}
