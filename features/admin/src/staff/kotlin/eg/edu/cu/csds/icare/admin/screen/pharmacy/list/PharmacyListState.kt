package eg.edu.cu.csds.icare.admin.screen.pharmacy.list

import eg.edu.cu.csds.icare.core.domain.model.Pharmacy

data class PharmacyListState(
    val isLoading: Boolean = false,
    val pharmacies: List<Pharmacy> = emptyList(),
    val effect: PharmacyListEffect? = null,
)
