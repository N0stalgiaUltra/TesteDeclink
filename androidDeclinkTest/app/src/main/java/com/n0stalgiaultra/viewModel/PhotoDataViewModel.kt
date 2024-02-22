package com.n0stalgiaultra.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.domain.usecases.GetAllPhotoDataUseCase
import com.n0stalgiaultra.domain.usecases.GetPhotoDataUseCase
import com.n0stalgiaultra.domain.usecases.InsertPhotoDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoDataViewModel(
    private val insertPhotoDataUseCase: InsertPhotoDataUseCase,
    private val getPhotoDataUseCase: GetPhotoDataUseCase,
    private val getAllPhotoDataUseCase: GetAllPhotoDataUseCase
) : ViewModel() {

    private val _photoDataList = MutableLiveData<List<PhotoModel>?>()
    val photoDataList : LiveData<List<PhotoModel>?> get() = _photoDataList
    private lateinit var localPhotoModel : PhotoModel

    suspend fun insertPhotoData(photoModel: PhotoModel){
        localPhotoModel = photoModel

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                insertPhotoDataUseCase(photoModel)
            }
            // Após a inserção bem-sucedida, recuperamos os dados
            getAllPhotoData()
        }
        Log.d("ViewModel", "item inserido")
        Log.d("ViewModel", localPhotoModel.DATA_HORA.toString())
    }

    private fun getAllPhotoData(){
        _photoDataList.postValue(emptyList())
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _photoDataList.postValue(getAllPhotoDataUseCase())
            }
//            Log.d("ViewModel", _photoDataList.value!!.size.toString())
            prepareToSendData()
        }
    }

    suspend fun getPhotoData(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = getPhotoDataUseCase(0)
            }
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

        Log.d("ViewModel", localPhotoModel.ID_CAPTURA.toString())
    }

}
