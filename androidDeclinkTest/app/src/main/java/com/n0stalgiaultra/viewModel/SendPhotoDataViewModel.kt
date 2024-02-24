package com.n0stalgiaultra.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.domain.usecases.GetAllPhotoDataUseCase
import com.n0stalgiaultra.domain.usecases.SendRemoteDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SendPhotoDataViewModel(
    private val getAllPhotoDataUseCase: GetAllPhotoDataUseCase,
    private val sendRemoteDataUseCase: SendRemoteDataUseCase,
) : ViewModel() {
    private val _photoDataList = MutableLiveData<List<PhotoModel>?>()
    lateinit var localPhotoModel : PhotoModel

    private fun getAllPhotoData(){
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
                return
            }
        }

        if(localPhotoModel.ID_CAPTURA != 0)
            sendData()
        Log.d("ViewModel", localPhotoModel.ID_CAPTURA.toString())
    }

    private fun sendData(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                sendRemoteDataUseCase(localPhotoModel)
                Log.d("ViewModel", "Tentativa de envio dos dados")
                // TODO: DEPENDENDO DA RESPOSTA, ADICIONAR UM TOAST AVISANDO SE O ITEM FOI OU NÃO ENVIADO
            }
        }
    }
}