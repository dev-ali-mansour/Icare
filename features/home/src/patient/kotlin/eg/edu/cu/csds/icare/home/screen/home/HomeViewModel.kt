package eg.edu.cu.csds.icare.home.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import eg.edu.cu.csds.icare.core.domain.model.Promotion
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel : ViewModel() {
    val openDialog: MutableState<Boolean> = mutableStateOf(false)
    val isPlayed: MutableState<Boolean> = mutableStateOf(false)

    private val _promotionResFlow =
        MutableStateFlow<Resource<List<Promotion>>>(Resource.Unspecified())
    val promotionResFlow: StateFlow<Resource<List<Promotion>>> =
        _promotionResFlow.asStateFlow()

    init {
        _promotionResFlow.value =
            Resource.Success(
                listOf(
                    Promotion(
                        id = 1,
                        imageUrl = "https://i.postimg.cc/5jjyk7Jn/promo1.png",
                        discount = "30%",
                    ),
                    Promotion(
                        id = 2,
                        imageUrl = "https://i.postimg.cc/vDjTRrHM/promo2.png",
                        discount = "50%",
                    ),
                ),
            )
    }
}
