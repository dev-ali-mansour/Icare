package eg.edu.cu.csds.icare.consultation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.AddNewConsultation
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetImagingTestsByStatus
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetLabTestsByStatus
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetMedicalRecord
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetMedicationsByStatus
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.UpdateConsultation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ConsultationViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val addNewConsultationUseCase: AddNewConsultation,
    private val updateConsultationUseCase: UpdateConsultation,
    private val getMedicalRecordUseCase: GetMedicalRecord,
    private val getMedicationsByStatusUseCase: GetMedicationsByStatus,
    private val getLabTestsByStatusUseCase: GetLabTestsByStatus,
    private val getImagingTestsByStatusUseCase: GetImagingTestsByStatus,
) : ViewModel() {
    private val _actionResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val actionResFlow: SharedFlow<Resource<Nothing?>> =
        _actionResFlow.shareIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(stopTimeoutMillis = 5000),
            replay = 0,
        )
    private val _consultationsResFlow =
        MutableStateFlow<Resource<List<Consultation>>>(Resource.Unspecified())
    val consultationsResFlow: StateFlow<Resource<List<Consultation>>> = _consultationsResFlow
    private val _medicalRecordResFlow = MutableStateFlow<Resource<MedicalRecord>>(Resource.Unspecified())
    val medicalRecordResFlow: StateFlow<Resource<MedicalRecord>> = _medicalRecordResFlow
    var selectedConsultationState: MutableState<Consultation?> = mutableStateOf(null)
        set(value) {
            appointmentState.value = value.value?.appointment ?: Appointment()
            field.value = value.value
        }

    var appointmentState = mutableStateOf(Appointment())
    var diagnosisState = mutableStateOf("")
    var pharmacyIdState = mutableLongStateOf(1)
    var medicationsState = mutableStateOf("")
    var prescriptionStatusIdState: MutableState<Short> = mutableStateOf(1)
    var labCenterIdState = mutableLongStateOf(1)
    var labTestsState = mutableStateOf("")
    var labTestStatusIdState: MutableState<Short> = mutableStateOf(1)
    var imagingCenterIdState = mutableLongStateOf(1)
    var imagingTestsState = mutableStateOf("")
    var imgTestStatusIdState: MutableState<Short> = mutableStateOf(1)
    var followUpdDateState = mutableLongStateOf(0)
    var labCentersExpandedState = mutableStateOf(false)
    var imagingCentersExpandedState = mutableStateOf(false)

    // Todo Remove this mock data
    private val mockResource =
        Resource.Success(
            listOf(
                Consultation(
                    appointment =
                        Appointment(
                            id = 1,
                            doctorName = "محمد السيد عثمان",
                            doctorImage =
                                "https://t4.ftcdn.net/jpg/01/98/82/75/360_F_" +
                                    "198827520_wVNNHdMq4yLJe76WWivQQ5Ev2WtXac4N.webp",
                            dateTime =
                                System
                                    .currentTimeMillis()
                                    .minus(other = 1000 * 60 * 60 * 24 * 3),
                        ),
                ),
            ),
        )

    fun addNewConsultation() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            addNewConsultationUseCase(
                Consultation(
                    appointment = appointmentState.value,
                    dateTime = System.currentTimeMillis(),
                    diagnosis = diagnosisState.value,
                    pharmacyId = pharmacyIdState.longValue,
                    medications = medicationsState.value,
                    prescriptionStatusId = prescriptionStatusIdState.value,
                    labCenterId = labCenterIdState.longValue,
                    labTests = labTestsState.value,
                    labTestStatusId = labTestStatusIdState.value,
                    imagingCenterId = imagingCenterIdState.longValue,
                    imagingTests = imagingTestsState.value,
                    imgTestStatusId = imgTestStatusIdState.value,
                    followUpdDate = followUpdDateState.longValue,
                ),
            ).collect { result ->
                _actionResFlow.emit(result)
            }
        }
    }

    fun updateConsultation() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            selectedConsultationState.value?.let {
                updateConsultationUseCase(it).collect { result ->
                    _actionResFlow.emit(result)
                }
            } ?: run {
                _actionResFlow.emit(Resource.Error(Error("No consultation selected!")))
            }
        }
    }

    fun getMedicalRecord(patientId: String) {
        viewModelScope.launch(dispatcher) {
            if (_medicalRecordResFlow.value !is Resource.Unspecified) {
                _medicalRecordResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }

            getMedicalRecordUseCase(patientId).collect { result ->
                // Todo Remove this mock data
                if (result is Resource.Error) {
                    _medicalRecordResFlow.value =
                        Resource.Success(
                            MedicalRecord(
                                patientName = "محمد السيد عثمان",
                                patientImage = "https://t.pimg.jp/047/354/467/1/47354467.jpg",
                                gender = 'M',
                                chronicDiseases = "مرض السكري",
                                currentMedications = "أدوية السكري",
                                allergies = "حساسية ضد الفول السوداني",
                                pastSurgeries = "لا يوجد",
                                weight = 80.5,
                                consultations =
                                    listOf(
                                        Consultation(
                                            dateTime =
                                                System
                                                    .currentTimeMillis()
                                                    .minus(other = 1000 * 60 * 60 * 24 * 3),
                                            appointment =
                                                Appointment(
                                                    id = 1,
                                                    doctorName = "د.سيد عبد الحليم الجوهري",
                                                    doctorImage =
                                                        "https://t4.ftcdn.net/jpg/01/98/82/75/360_F_" +
                                                            "198827520_wVNNHdMq4yLJe76WWivQQ5Ev2WtXac4N.webp",
                                                ),
                                        ),
                                    ),
                            ),
                        )
                } else {
                    _medicalRecordResFlow.value = result
                }
            }
        }
    }

    fun getMedicationsByStatus(statusId: Short) {
        viewModelScope.launch(dispatcher) {
            if (_consultationsResFlow.value !is Resource.Unspecified) {
                _consultationsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }

            getMedicationsByStatusUseCase(statusId).collect { result ->
                // Todo Remove this mock data
                if (result is Resource.Error) {
                    _consultationsResFlow.value = mockResource
                } else {
//                    _consultationsResFlow.value = result
                }
            }
        }
    }

    fun getLabTestsByStatus(statusId: Short) {
        viewModelScope.launch(dispatcher) {
            if (_consultationsResFlow.value !is Resource.Unspecified) {
                _consultationsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }

            getLabTestsByStatusUseCase(statusId).collect { result ->
                // Todo Remove this mock data
                if (result is Resource.Error) {
                    _consultationsResFlow.value = mockResource
                } else {
//                    _consultationsResFlow.value = result
                }
            }
        }
    }

    fun getImagingTestsByStatus(statusId: Short) {
        viewModelScope.launch(dispatcher) {
            if (_consultationsResFlow.value !is Resource.Unspecified) {
                _consultationsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }

            getImagingTestsByStatusUseCase(statusId).collect { result ->
                // Todo Remove this mock data
                if (result is Resource.Error) {
                    _consultationsResFlow.value = mockResource
                } else {
//                    _consultationsResFlow.value = result
                }
            }
        }
    }
}
