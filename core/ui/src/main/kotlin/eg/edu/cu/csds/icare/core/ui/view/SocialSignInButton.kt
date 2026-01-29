package eg.edu.cu.csds.icare.core.ui.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.MEDIUM_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.TabRowContainerColor
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING

@Composable
fun SocialSignInButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
) {
    Surface(
        modifier =
            modifier
                .size(ButtonDefaults.MinWidth, ButtonDefaults.MinHeight)
                .clickable { onClick() },
        color = TabRowContainerColor,
        shape = RoundedCornerShape(topEnd = S_PADDING, bottomStart = S_PADDING),
    ) {
        Column(
            modifier = Modifier.padding(XS_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                modifier = Modifier.size(MEDIUM_ICON_SIZE),
                painter = painterResource(iconId),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun SocialSignInButtonPreview() {
    IcareTheme {
        Box(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(M_PADDING),
            contentAlignment = Alignment.Center,
        ) {
            SocialSignInButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.7f),
                iconId = R.drawable.core_ui_ic_social_google,
            ) { }
        }
    }
}
