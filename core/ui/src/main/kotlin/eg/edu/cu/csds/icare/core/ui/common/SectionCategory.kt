package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SectionCategory(
    @param:StringRes
    val titleResId: Int,
    val sections: List<SectionItem>,
)

data class SectionItem(
    @param:DrawableRes val iconResId: Int,
    @param:StringRes val titleResId: Int,
)
