package com.n0stalgiaultra.domain.usecases

import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.domain.repository.LocalRepository

class GetPhotoDataUseCase(private val repository: LocalRepository) {
    suspend operator fun invoke(id: Int): PhotoModel {
        return repository.getPhotoData(id)
    }
}