package eg.edu.cu.csds.icare.feature.home.screen.pharmacy

sealed interface PharmacyListEvent {
    object OnBackClick : PharmacyListEvent

    object Refresh : PharmacyListEvent

    data class UpdateSearchQuery(
        val query: String,
    ) : PharmacyListEvent

    object Search : PharmacyListEvent

    object ConsumeEffect : PharmacyListEvent
}
