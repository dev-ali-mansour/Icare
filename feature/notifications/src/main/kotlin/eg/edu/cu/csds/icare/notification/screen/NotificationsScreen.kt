package eg.edu.cu.csds.icare.notification.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor

@Composable
internal fun NotificationsScreen() {
    Column(
        modifier =
            Modifier
                .background(backgroundColor)
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        NotificationsContent()
    }
}
