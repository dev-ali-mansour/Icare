package eg.edu.cu.csds.icare.home.screen.lab

sealed interface LabListEvent {
    object OnBackClick : LabListEvent

    object Refresh : LabListEvent

    data class UpdateSearchQuery(
        val query: String,
    ) : LabListEvent

    object Search : LabListEvent

    object ConsumeEffect : LabListEvent
}
