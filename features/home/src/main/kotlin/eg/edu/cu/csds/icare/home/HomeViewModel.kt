package eg.edu.cu.csds.icare.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel : ViewModel() {
    val openDialog: MutableState<Boolean> = mutableStateOf(false)
    val isPlayed: MutableState<Boolean> = mutableStateOf(false)
}
