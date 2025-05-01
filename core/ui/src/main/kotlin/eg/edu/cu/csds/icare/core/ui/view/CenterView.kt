package eg.edu.cu.csds.icare.core.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.ui.common.CenterTypeItem
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROFILE_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.cardBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
fun CenterView(
    center: LabImagingCenter,
    modifier: Modifier = Modifier,
    showType: Boolean = false,
    onClick: () -> Unit,
) {
    val types = listOf(CenterTypeItem.ImagingCenter, CenterTypeItem.LabCenter)
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
                    .padding(M_PADDING),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter =
                    painterResource(
                        types.firstOrNull { it.code == center.type }?.iconResId
                            ?: types.first().iconResId,
                    ),
                contentDescription = null,
                modifier =
                    Modifier
                        .align(Alignment.Top)
                        .padding(XS_PADDING)
                        .clip(CircleShape)
                        .size(PROFILE_IMAGE_SIZE),
                contentScale = ContentScale.FillBounds,
            )

            Spacer(modifier = Modifier.width(M_PADDING))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = center.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = helveticaFamily,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                if (showType) {
                    Spacer(modifier = Modifier.height(S_PADDING))
                    Text(
                        text =
                            stringResource(
                                types.firstOrNull { it.code == center.type }?.textResId
                                    ?: types.first().textResId,
                            ),
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = helveticaFamily,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                    )
                }

                Spacer(modifier = Modifier.height(S_PADDING))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null)
                    Spacer(modifier = Modifier.width(XS_PADDING))
                    Text(text = center.phone)
                }
                Spacer(modifier = Modifier.height(M_PADDING))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null)
                    Spacer(modifier = Modifier.width(XS_PADDING))
                    Text(text = center.address)
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
fun CenterCardPreview() {
    MaterialTheme {
        Column(
            modifier =
                Modifier
                    .padding(XS_PADDING)
                    .background(color = backgroundColor),
        ) {
            CenterView(
                showType = true,
                center =
                    LabImagingCenter(
                        type = 1,
                        name = "Alfa",
                        phone = "0123456789",
                        address = "53 james street,Giza,Egypt",
                    ),
                onClick = {},
            )
            CenterView(
                center =
                    LabImagingCenter(
                        type = 2,
                        name = "Beta",
                        phone = "0123456789",
                        address = "53 james street,Giza,Egypt",
                    ),
                onClick = {},
            )
        }
    }
}
