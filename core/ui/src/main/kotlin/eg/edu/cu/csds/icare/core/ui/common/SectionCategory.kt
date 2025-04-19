package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SectionCategory(
    @StringRes
    val titleResId: Int,
    val sections: List<SectionItem>,
)

data class SectionItem(
    @DrawableRes val iconResId: Int,
    @StringRes val titleResId: Int,
)
