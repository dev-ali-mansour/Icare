package eg.edu.cu.csds.icare.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import eg.edu.cu.csds.icare.core.domain.model.SettingsItem
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.util.getSettingsItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel : ViewModel() {
    private val _settingsFlow = MutableStateFlow<List<SettingsItem>>(listOf())
    val settingsFlow: StateFlow<List<SettingsItem>> get() = _settingsFlow

    fun getSettingsItems(context: Context) {
        val items = context.getSettingsItems()
        _settingsFlow.value = items
    }

    fun addAdminSection(context: Context) {
        val items = _settingsFlow.value.toMutableList()
        items.add(
            index = 1,
            element =
                SettingsItem(
                    Constants.ADMIN_CODE,
                    context.getString(R.string.sections_admin),
                ),
        )
        _settingsFlow.value = items
    }
}
