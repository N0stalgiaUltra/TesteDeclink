package com.n0stalgiaultra.domain.usecases

import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.domain.repository.LocalRepository

class GetAllPhotoDataUseCase(private val repository: LocalRepository) {
    suspend operator fun invoke() : List<PhotoModel>{
        return repository.getAllPhotoData()
    }
}