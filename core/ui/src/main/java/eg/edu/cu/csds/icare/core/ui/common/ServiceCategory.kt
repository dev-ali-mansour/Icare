package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

data class ServiceCategory(
    @StringRes
    val titleResId: Int,
    val services: List<ServiceItem>,
)

data class ServiceItem(
    @DrawableRes
    val iconResId: Int,
    @StringRes
    val titleResId: Int,
    val screen: Screen,
)
