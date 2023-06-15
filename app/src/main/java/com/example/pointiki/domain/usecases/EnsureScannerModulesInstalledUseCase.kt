package com.example.pointiki.domain.usecases

import android.content.Context
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class EnsureScannerModulesInstalledUseCase @Inject constructor(
    private val context: Context,
    private val scanner: GmsBarcodeScanner
) {
    operator fun invoke(): Flow<Boolean> = flow {
        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
            .addApi(scanner)
            .build()

        val moduleInstallClient = ModuleInstall.getClient(context)

        val task = moduleInstallClient.installModules(moduleInstallRequest)

        val result = suspendCancellableCoroutine<Boolean> { continuation ->
            task.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result?.areModulesAlreadyInstalled() ?: false) {
                        // Handle cancellation
                        task.exception?.let { e ->

                        }
                    }
                } else {
                    continuation.resumeWithException(task.exception ?: RuntimeException("Unknown error"))
                }
            }
        }
        emit(result)
    }
}