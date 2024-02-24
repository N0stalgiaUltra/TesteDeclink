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

class SavePhotoDataViewModel(
    private val insertPhotoDataUseCase: InsertPhotoDataUseCase,
) : ViewModel() {

    suspend fun insertPhotoData(photoModel: PhotoModel){

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                insertPhotoDataUseCase(photoModel)
            }
        }
    }

}
