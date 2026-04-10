package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class OnBoardingPage(
    @param:DrawableRes
    val image: Int,
    val title: UiText,
    val description: UiText,
)
