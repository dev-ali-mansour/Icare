package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.StringRes
import eg.edu.cu.csds.icare.core.ui.R

sealed class CenterTypeItem(
    val code: Short,
    @StringRes
    val textResId: Int,
) {
    data object ImagingCenter : CenterTypeItem(code = 1, textResId = R.string.imaging_center)

    data object LabCenter : CenterTypeItem(code = 2, textResId = R.string.lab_center)
}
