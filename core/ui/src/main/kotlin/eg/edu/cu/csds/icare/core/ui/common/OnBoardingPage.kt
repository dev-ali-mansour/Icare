package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.DrawableRes

data class OnBoardingPage(
    @param:DrawableRes
    val image: Int,
    val title: String,
    val description: String,
)
