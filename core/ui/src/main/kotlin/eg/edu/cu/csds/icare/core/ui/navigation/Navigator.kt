package eg.edu.cu.csds.icare.core.ui.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

@Stable
class Navigator(
    startDestination: Any,
) {
    val backStack: SnapshotStateList<Any> = mutableStateListOf(startDestination)

    fun goTo(destination: Any) {
        backStack.add(destination)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }

    fun clearBackStack() {
        backStack.clear()
    }
}
