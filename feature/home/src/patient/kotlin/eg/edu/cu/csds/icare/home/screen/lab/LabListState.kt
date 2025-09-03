package eg.edu.cu.csds.icare.home.screen.lab

import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter

data class LabListState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val labs: List<LabImagingCenter> = emptyList(),
    val effect: LabListEffect? = null,
)
