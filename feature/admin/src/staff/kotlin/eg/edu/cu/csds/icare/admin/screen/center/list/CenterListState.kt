package eg.edu.cu.csds.icare.admin.screen.center.list

import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter

data class CenterListState(
    val isLoading: Boolean = false,
    val centers: List<LabImagingCenter> = emptyList(),
    val effect: CenterListEffect? = null,
)
