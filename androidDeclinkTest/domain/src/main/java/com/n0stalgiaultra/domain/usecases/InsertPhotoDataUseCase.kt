package com.n0stalgiaultra.domain.usecases

import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.domain.repository.LocalRepository

class InsertPhotoDataUseCase(private val repository: LocalRepository) {
    suspend operator fun invoke(item: PhotoModel){
        repository.insertPhotoData(item)
    }
}