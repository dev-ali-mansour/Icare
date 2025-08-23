package eg.edu.cu.csds.icare.admin.screen.pharmacist.list

import eg.edu.cu.csds.icare.core.domain.model.Pharmacist

data class PharmacistListState(
    val isLoading: Boolean = false,
    val pharmacists: List<Pharmacist> = emptyList(),
    val effect: PharmacistListEffect? = null,
)
