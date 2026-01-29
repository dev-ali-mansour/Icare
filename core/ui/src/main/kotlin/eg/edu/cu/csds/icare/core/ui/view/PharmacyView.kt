package eg.edu.cu.csds.icare.core.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.MAX_SURFACE_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.util.neumorphicUp

@Composable
fun PharmacyView(
    name: String,
    phone: String,
    address: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
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
                painter = painterResource(R.drawable.core_ui_ic_pharmacy),
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
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(S_PADDING))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null)
                    Spacer(modifier = Modifier.width(XS_PADDING))
                    Text(text = phone)
                }

                Spacer(modifier = Modifier.height(M_PADDING))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null)
                    Spacer(modifier = Modifier.width(XS_PADDING))
                    Text(text = address)
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PharmacyCardPreview() {
    IcareTheme {
        Column(
            modifier =
                Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(S_PADDING),
        ) {
            PharmacyView(
                name = "Pharmacy 1",
                address = "Address 1",
                phone = "123456789",
                onClick = {},
            )

            Spacer(modifier = Modifier.height(S_PADDING))

            PharmacyView(
                name = "Pharmacy 2",
                address = "Address 2",
                phone = "123456789",
                onClick = {},
            )
        }
    }
}
