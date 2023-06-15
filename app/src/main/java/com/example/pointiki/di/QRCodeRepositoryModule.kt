package com.example.pointiki.di

import com.example.pointiki.data.repo.QrCodeRepositoryImpl
import com.example.pointiki.domain.repo.QrCodeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class QRCodeRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindMainRepo(
        qrCodeRepoImpl: QrCodeRepositoryImpl
    ): QrCodeRepository

}


