package eg.edu.cu.csds.icare.home.screen.imaging

import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter

data class ImagingCenterListState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val centers: List<LabImagingCenter> = emptyList(),
    val effect: ImagingCenterListEffect? = null,
)
