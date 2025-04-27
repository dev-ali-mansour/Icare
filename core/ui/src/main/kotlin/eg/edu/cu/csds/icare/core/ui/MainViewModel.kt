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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
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
                getUserInfoUseCase(forceUpdate = true).distinctUntilChanged().collect {
                    _currentUserFlow.value = it
                }
                readOnBoardingUseCase().collect { res ->
                    _onBoardingCompleted.value = res

                    if (res is Resource.Success) {

                        if (_resultFlow.value !is Resource.Unspecified) {
                            _resultFlow.value = Resource.Unspecified()
                            delay(timeMillis = 100)
                        }
                        listClinicsUseCase(forceUpdate = true).collect {
                            when (it) {
                                is Resource.Unspecified<*> ->
                                    _resultFlow.value = Resource.Unspecified()

                                is Resource.Loading<*> ->
                                    _resultFlow.value = Resource.Loading()

                                is Resource.Success ->
                                    listDoctorsUseCase(forceUpdate = true).collect {
                                        when (it) {
                                            is Resource.Unspecified<*> ->
                                                _resultFlow.value =
                                                    Resource.Unspecified()

                                            is Resource.Loading<*> ->
                                                _resultFlow.value = Resource.Loading()

                                            is Resource.Success ->
                                                listPharmaciesUseCase(forceUpdate = true).collect {
                                                    when (it) {
                                                        is Resource.Unspecified<*> ->
                                                            _resultFlow.value =
                                                                Resource.Unspecified()

                                                        is Resource.Loading<*> ->
                                                            _resultFlow.value = Resource.Loading()

                                                        is Resource.Success ->
                                                            listCentersUseCase(forceUpdate = true).collect {
                                                                when (it) {
                                                                    is Resource.Unspecified<*> ->
                                                                        _resultFlow.value =
                                                                            Resource.Unspecified()

                                                                    is Resource.Loading<*> ->
                                                                        _resultFlow.value =
                                                                            Resource.Loading()

                                                                    is Resource.Success ->
                                                                        _resultFlow.value =
                                                                            Resource.Success(null)

                                                                    is Resource.Error<*> ->
                                                                        _resultFlow.value =
                                                                            Resource.Error(it.error)
                                                                }
                                                            }

                                                        is Resource.Error<*> ->
                                                            _resultFlow.value =
                                                                Resource.Error(it.error)
                                                    }
                                                }

                                            is Resource.Error<*> ->
                                                _resultFlow.value = Resource.Error(it.error)
                                        }
                                    }

                                is Resource.Error<*> ->
                                    // Todo Remove this mock data
                                    _resultFlow.value = Resource.Success(null)
//                                    _resultFlow.value = Resource.Error(it.error)
                            }
                        }
                        coroutineScope {
                            val clinicsDeferred = async { listClinicsUseCase(forceUpdate = true).first() }
                            val doctorsDeferred = async { listDoctorsUseCase(forceUpdate = true).first() }
                            val pharmaciesDeferred = async { listPharmaciesUseCase(forceUpdate = true).first() }
                            val centersDeferred = async { listCentersUseCase(forceUpdate = true).first() }

                            // Wait for all requests to complete
                            val results =
                                awaitAll(
                                    clinicsDeferred,
                                    doctorsDeferred,
                                    pharmaciesDeferred,
                                    centersDeferred,
                                )

                            results.forEach { result ->
                                when (result) {
                                    is Resource.Error -> {
                                        _resultFlow.value = Resource.Error(result.error)
                                        return@coroutineScope
                                    }
                                    is Resource.Loading,
                                    is Resource.Unspecified,
                                    -> {
                                        _resultFlow.value = Resource.Loading()
                                        return@coroutineScope
                                    }
                                    is Resource.Success -> { }
                                }
                            }

                            _resultFlow.value = Resource.Success(null)
                        }
                    }
                }
            }.onFailure { _resultFlow.value = Resource.Error(it) }
        }
    }
}
