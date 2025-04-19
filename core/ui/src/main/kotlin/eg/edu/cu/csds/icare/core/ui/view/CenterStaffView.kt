package eg.edu.cu.csds.icare.core.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.ui.theme.DOUBLE_THICK_BORDER_STROKE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.PaidColor
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.itemBackgroundColor

@Composable
fun CenterStaffView(
    modifier: Modifier = Modifier,
    staff: CenterStaff,
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
            Text(text = staff.name)
        }
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CenterStaffViewPreview() {
    Column(
        modifier =
            Modifier
                .padding(XS_PADDING)
                .background(color = backgroundColor),
    ) {
        CenterStaffView(staff = CenterStaff())
    }
}
