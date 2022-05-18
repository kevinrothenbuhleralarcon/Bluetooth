package ch.kra.bluetooth.bluetooth.presentation.device_control

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.bluetooth.core.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceControlViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _deviceAddress = mutableStateOf("")
    val deviceAddress: State<String> = _deviceAddress

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        _deviceAddress.value = savedStateHandle.get<String>("deviceAddress") ?: ""
    }

    fun onEvent(event: DeviceControlListEvent) {
        when (event) {
            is DeviceControlListEvent.OnOnClick -> {

            }

            is DeviceControlListEvent.OnOffClick -> {

            }

            is DeviceControlListEvent.OnDisconnectClick -> {

            }

            is DeviceControlListEvent.OnBackPressed -> {
                sendUIEvent(UIEvent.NavigateBack)
            }
        }
    }


    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}