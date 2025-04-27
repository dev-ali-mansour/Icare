package eg.edu.cu.csds.icare.core.ui

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfo
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCenters
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinics
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctors
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.ReadOnBoarding
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmacies
import eg.edu.cu.csds.icare.core.ui.common.SectionCategory
import eg.edu.cu.csds.icare.core.ui.common.SectionItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    dispatcher: CoroutineDispatcher,
    private val readOnBoardingUseCase: ReadOnBoarding,
    private val getUserInfoUseCase: GetUserInfo,
    private val listClinicsUseCase: ListClinics,
    private val listDoctorsUseCase: ListDoctors,
    private val listPharmaciesUseCase: ListPharmacies,
    private val listCentersUseCase: ListCenters,
) : ViewModel() {
    private val _onBoardingCompleted: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Unspecified())
    val onBoardingCompleted: StateFlow<Resource<Boolean>> = _onBoardingCompleted
    private val _currentUserFlow: MutableStateFlow<Resource<User>> =
        MutableStateFlow(Resource.Unspecified())
    val currentUserFlow: StateFlow<Resource<User>> = _currentUserFlow
    private val _resultFlow = MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val resultFlow: StateFlow<Resource<Nothing?>> = _resultFlow

    var selectedCategoryTabIndex = mutableIntStateOf(0)
    var selectedSectionTabIndex = mutableIntStateOf(0)

    val adminCategories =
        listOf(
            SectionCategory(
                titleResId = R.string.clinics,
                sections =
                    listOf(
                        SectionItem(
                            iconResId = R.drawable.ic_clinic,
                            titleResId = R.string.clinics,
                        ),
                        SectionItem(
                            iconResId = R.drawable.ic_doctor,
                            titleResId = R.string.doctors,
                        ),
                        SectionItem(
                            iconResId = R.drawable.ic_staff,
                            titleResId = R.string.clinic_staffs,
                        ),
                    ),
            ),
            SectionCategory(
                titleResId = R.string.pharmacies,
                sections =
                    listOf(
                        SectionItem(
                            iconResId = R.drawable.ic_pharmacy,
                            titleResId = R.string.pharmacies,
                        ),
                        SectionItem(
                            iconResId = R.drawable.ic_pharmacist,
                            titleResId = R.string.pharmacists,
                        ),
                    ),
            ),
            SectionCategory(
                titleResId = R.string.centers,
                sections =
                    listOf(
                        SectionItem(
                            iconResId = R.drawable.ic_lab,
                            titleResId = R.string.centers,
                        ),
                        SectionItem(
                            iconResId = R.drawable.ic_staff2,
                            titleResId = R.string.center_staffs,
                        ),
                    ),
            ),
        )

    init {
        viewModelScope.launch(dispatcher) {
            runCatching {
                getUserInfoUseCase(forceUpdate = true)
                    .distinctUntilChanged()
                    .onEach { _currentUserFlow.value = it }
                    .collect {}

                readOnBoardingUseCase()
                    .onEach { res ->
                        _onBoardingCompleted.value = res

                        if (res is Resource.Success) {
                            _resultFlow.value = Resource.Loading()
                            delay(timeMillis = 100)

                            val clinicsFlow =
                                listClinicsUseCase(forceUpdate = true)
                                    .onStart { emit(Resource.Loading()) }
                                    .catch { emit(Resource.Error(it)) }
                            val doctorsFlow =
                                listDoctorsUseCase(forceUpdate = true)
                                    .onStart { emit(Resource.Loading()) }
                                    .catch { emit(Resource.Error(it)) }
                            val pharmaciesFlow =
                                listPharmaciesUseCase(forceUpdate = true)
                                    .onStart { emit(Resource.Loading()) }
                                    .catch { emit(Resource.Error(it)) }
                            val centersFlow =
                                listCentersUseCase(forceUpdate = true)
                                    .onStart { emit(Resource.Loading()) }
                                    .catch { emit(Resource.Error(it)) }

                            combine(
                                clinicsFlow,
                                doctorsFlow,
                                pharmaciesFlow,
                                centersFlow,
                            ) { clinics, doctors, pharmacies, centers ->
                                val errors =
                                    listOf(
                                        clinics,
                                        doctors,
                                        pharmacies,
                                        centers,
                                    ).filterIsInstance<Resource.Error<Nothing?>>()
                                val loading =
                                    listOf(
                                        clinics,
                                        doctors,
                                        pharmacies,
                                        centers,
                                    ).filterIsInstance<Resource.Loading<Nothing?>>()

                                when {
                                    errors.isNotEmpty() -> errors.first()
                                    loading.isNotEmpty() -> Resource.Loading()
                                    else -> Resource.Success(null)
                                }
                            }.collect {
                                _resultFlow.value = it
                            }
                        } else {
                            _resultFlow.value = Resource.Unspecified()
                        }
                    }.collect {}
            }.onFailure {
                _resultFlow.value = Resource.Error(it)
            }
        }
    }
}
