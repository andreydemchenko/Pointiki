package com.example.pointiki.domain.repo

import kotlinx.coroutines.flow.Flow

interface QrCodeRepository {
    fun startScanning(): Flow<String?>
}