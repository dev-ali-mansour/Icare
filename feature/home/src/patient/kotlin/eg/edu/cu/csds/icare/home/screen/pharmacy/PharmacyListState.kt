package eg.edu.cu.csds.icare.home.screen.pharmacy

import eg.edu.cu.csds.icare.core.domain.model.Pharmacy

data class PharmacyListState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val pharmacies: List<Pharmacy> = emptyList(),
    val effect: PharmacyListEffect? = null,
)
