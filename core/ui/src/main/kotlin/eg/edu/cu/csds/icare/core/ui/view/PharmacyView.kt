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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.cardBackgroundColor

@Composable
fun PharmacyView(
    pharmacy: Pharmacy,
    modifier: Modifier = Modifier,
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
                    .padding(M_PADDING),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_pharmacy),
                contentDescription = null,
                modifier =
                    Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .align(Alignment.Top),
            )

            Spacer(modifier = Modifier.width(M_PADDING))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(XS_PADDING),
            ) {
                Text(
                    text = pharmacy.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(S_PADDING))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null)
                    Spacer(modifier = Modifier.width(XS_PADDING))
                    Text(text = pharmacy.phone)
                }
                Spacer(modifier = Modifier.height(M_PADDING))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null)
                    Spacer(modifier = Modifier.width(XS_PADDING))
                    Text(text = pharmacy.address)
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
fun PharmacyCardPreview() {
    Column(
        modifier =
            Modifier
                .padding(XS_PADDING)
                .background(color = backgroundColor),
    ) {
        PharmacyView(
            pharmacy =
                Pharmacy(
                    id = 1,
                    name = "Pharmacy 1",
                    address = "Address 1",
                    phone = "123456789",
                ),
            onClick = {},
        )

        Spacer(modifier = Modifier.height(XS_PADDING))

        PharmacyView(
            pharmacy =
                Pharmacy(
                    id = 2,
                    name = "Pharmacy 2",
                    address = "Address 2",
                    phone = "123456789",
                ),
            onClick = {},
        )
    }
}
