package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.StringRes

data class GenderItem(
    val code: Short,
    @StringRes
    val textResId: Int,
)
