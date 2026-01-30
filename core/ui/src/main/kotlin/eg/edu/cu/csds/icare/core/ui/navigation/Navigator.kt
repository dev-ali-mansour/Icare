package eg.edu.cu.csds.icare.core.ui.navigation

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

@Stable
class Navigator(
    val backStack: NavBackStack<NavKey>,
) {
    val currentScreen: NavKey?
        get() = backStack.lastOrNull()

    fun navigate(
        key: NavKey,
        inclusive: Boolean = false,
    ) {
        if (inclusive) backStack.clear()
        backStack.add(key)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }
}
