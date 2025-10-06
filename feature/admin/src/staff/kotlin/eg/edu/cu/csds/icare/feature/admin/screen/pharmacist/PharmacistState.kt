package eg.edu.cu.csds.icare.feature.admin.screen.pharmacist

import eg.edu.cu.csds.icare.core.domain.model.Pharmacy

data class PharmacistState(
    val isLoading: Boolean = false,
    val pharmacies: List<Pharmacy> = emptyList(),
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val name: String = "$firstName $lastName",
    val pharmacyId: Long = 0,
    val isPharmaciesExpanded: Boolean = false,
    val pharmacyName: String = "",
    val email: String = "",
    val phone: String = "",
    val profilePicture: String = "",
    val effect: PharmacistEffect? = null,
)
