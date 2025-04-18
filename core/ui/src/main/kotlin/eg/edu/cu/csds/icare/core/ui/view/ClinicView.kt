package eg.edu.cu.csds.icare.core.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.ui.theme.DOUBLE_THICK_BORDER_STROKE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PaidColor
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.UnPaidColor
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.itemBackgroundColor

@Composable
fun ClinicView(
    modifier: Modifier = Modifier,
    clinic: Clinic,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .alpha(alpha = if (!isSystemInDarkTheme()) 0.5f else 1f),
        color = itemBackgroundColor,
        shape = RoundedCornerShape(S_PADDING),
        border = BorderStroke(DOUBLE_THICK_BORDER_STROKE_SIZE, PaidColor),
    ) {
        ConstraintLayout(
            modifier =
                modifier
                    .fillMaxWidth()
                    .background(contentBackgroundColor),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(M_PADDING),
            ) {
                Text(
                    text = clinic.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(S_PADDING))

                Text(
                    text = clinic.type,
                    style = MaterialTheme.typography.bodyLarge,
                )

                Spacer(modifier = Modifier.height(S_PADDING))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null)
                    Spacer(modifier = Modifier.width(XS_PADDING))
                    Text(text = clinic.phone)
                }

                Spacer(modifier = Modifier.height(M_PADDING))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null)
                    Spacer(modifier = Modifier.width(XS_PADDING))
                    Text(text = clinic.address)
                }

                Spacer(modifier = Modifier.height(S_PADDING))

                Text(
                    text = if (clinic.isOpen) "Open" else "Closed",
                    color = if (clinic.isOpen) PaidColor else UnPaidColor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
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
fun ClinicViewPreview() {
    Column(
        modifier =
            Modifier
                .padding(XS_PADDING)
                .background(color = backgroundColor),
    ) {
        ClinicView(
            clinic =
                Clinic(
                    id = 1,
                    name = "Clinic 1",
                    type = "Type 1",
                    address = "Address 1",
                    phone = "123456789",
                    isOpen = true,
                ),
        )

        Spacer(modifier = Modifier.height(XS_PADDING))

        ClinicView(
            clinic =
                Clinic(
                    id = 2,
                    name = "Clinic 2",
                    type = "Type 2",
                    address = "Address 2",
                    phone = "987654321",
                    isOpen = false,
                ),
        )
    }
}
