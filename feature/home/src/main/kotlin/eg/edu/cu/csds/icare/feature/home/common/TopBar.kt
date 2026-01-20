package eg.edu.cu.csds.icare.feature.home.common

sealed class TopBar {
    data object ServiceTopBar : TopBar()

    data object ServiceSearchTopBar : TopBar()
}
