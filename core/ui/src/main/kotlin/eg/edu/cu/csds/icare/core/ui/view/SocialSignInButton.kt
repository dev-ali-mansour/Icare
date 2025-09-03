package eg.edu.cu.csds.icare.core.ui.view

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.MEDIUM_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor

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
        color = contentBackgroundColor,
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

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun SocialSignInButtonPreview() {
    SocialSignInButton(iconId = R.drawable.core_ui_ic_social_google) { }
}
