package com.n0stalgiaultra.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.domain.usecases.GetAllPhotoDataUseCase
import com.n0stalgiaultra.domain.usecases.SendRemoteDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SendPhotoDataViewModel(
    private val getAllPhotoDataUseCase: GetAllPhotoDataUseCase,
    private val sendRemoteDataUseCase: SendRemoteDataUseCase,
) : ViewModel() {
    private val _photoDataList = MutableLiveData<List<PhotoModel>?>()
    lateinit var localPhotoModel : PhotoModel

    private val _sendDataResult = MutableLiveData<Result<Unit>?>()
    val sendDataResult: LiveData<Result<Unit>?> get() = _sendDataResult

    /*Como não há API, o envio nunca vai ser sucesso*/
    fun getAllPhotoData(){
        _photoDataList.postValue(emptyList())
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _photoDataList.postValue(getAllPhotoDataUseCase())
            }
            prepareToSendData()
        }
    }

    private fun prepareToSendData(){
        for(item in _photoDataList.value!!){
            if(localPhotoModel.DATA_HORA == item.DATA_HORA &&
                localPhotoModel.CAMERA == item.CAMERA &&
                localPhotoModel.IP_EQUIPAMENTO == item.IP_EQUIPAMENTO
            ){

                Log.d("ViewModel", "ENCONTREI!")
                localPhotoModel.ID_CAPTURA = item.ID_CAPTURA
                Log.d("ViewModel", localPhotoModel.ID_CAPTURA.toString())
                break
            }
        }

        if(localPhotoModel.ID_CAPTURA != 0){
            viewModelScope.launch {
                delay(5000)
                sendData()
            }
        }
    }

    private suspend fun sendData(){
        Log.d("ViewModel", "Enviando dados")
        var currentAttempt = 0
        val maxAttempts = 5
        while (currentAttempt < maxAttempts) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    _sendDataResult.postValue(sendRemoteDataUseCase(localPhotoModel))
                    if(_sendDataResult.value?.isFailure == true) {
                        currentAttempt++
                        delay(1000)
                    }
                }
            }
        }
        Log.e("ViewModel", "Maximo de tenativas excedido")
    }

}