package eg.edu.cu.csds.icare.core.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.MediumGray
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark

@Composable
fun EmptyContentView(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.core_ui_no_available_data),
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(120.dp),
            painter = painterResource(id = R.drawable.core_ui_ic_sad_face),
            contentDescription = null,
            tint = MediumGray,
        )
        Text(
            text = text,
            color = MediumGray,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
        )
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@Composable
fun EmptyContentViewPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            EmptyContentView()
        }
    }
}
