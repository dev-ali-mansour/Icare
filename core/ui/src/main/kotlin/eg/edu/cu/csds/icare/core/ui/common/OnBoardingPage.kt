package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.DrawableRes
import eg.edu.cu.csds.icare.core.ui.util.UiText

data class OnBoardingPage(
    @param:DrawableRes
    val image: Int,
    val title: UiText,
    val description: UiText,
)
