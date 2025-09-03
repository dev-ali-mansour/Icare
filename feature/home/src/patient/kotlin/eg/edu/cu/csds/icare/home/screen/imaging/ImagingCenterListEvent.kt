package eg.edu.cu.csds.icare.home.screen.imaging

sealed interface ImagingCenterListEvent {
    object OnBackClick : ImagingCenterListEvent

    object Refresh : ImagingCenterListEvent

    data class UpdateSearchQuery(
        val query: String,
    ) : ImagingCenterListEvent

    object Search : ImagingCenterListEvent

    object ConsumeEffect : ImagingCenterListEvent
}
