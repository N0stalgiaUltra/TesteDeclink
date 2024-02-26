package com.n0stalgiaultra.domain.usecases

import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.domain.repository.RemoteRepository

class SendRemoteDataUseCase(private val remoteRepository: RemoteRepository) {
    suspend operator fun invoke(photoData: PhotoModel): Result<Unit>{
        return remoteRepository.sendDataToRemote(photoData)
    }
}