package eg.edu.cu.csds.icare.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import eg.edu.cu.csds.icare.core.ui.common.AppService
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.theme.GRID_ITEM_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.GRID_ITEM_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.SERVICE_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor

@Composable
fun ServiceItem(
    service: AppService,
    onClick: (Route) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .width(GRID_ITEM_WIDTH)
                .height(GRID_ITEM_HEIGHT)
                .clickable {
                    onClick(service.route)
                },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            painter = painterResource(id = service.iconResId),
            contentDescription = null,
            modifier = Modifier.size(SERVICE_ICON_SIZE),
            tint = Color.Unspecified,
        )
        Text(
            text = stringResource(service.textResId),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontFamily = helveticaFamily,
            color = textColor,
            maxLines = 2,
        )
    }
}
