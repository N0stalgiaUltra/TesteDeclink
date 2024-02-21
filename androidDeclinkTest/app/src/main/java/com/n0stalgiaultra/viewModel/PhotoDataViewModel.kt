package com.n0stalgiaultra.viewModel

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

    // um objeto, apenas
    private val _photoData = MutableLiveData<PhotoModel>()
    val photoData : LiveData<PhotoModel> get() = _photoData

    private val _photoDataList = MutableLiveData<List<PhotoModel>>()
    val photoDataList : LiveData<List<PhotoModel>> get() = _photoDataList


    suspend fun insertPhotoData(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                photoData.value?.let { insertPhotoDataUseCase(it) }
            }
        }
    }

    suspend fun getPhotoData(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = getPhotoDataUseCase(0)
            }
        }
    }
}

// tira foto
// recebe dados
// guarda em obj
// salva no BD.