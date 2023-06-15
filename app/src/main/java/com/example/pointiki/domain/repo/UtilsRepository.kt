package com.example.pointiki.domain.repo

import com.example.pointiki.utils.Result
import java.io.InputStream

interface UtilsRepository {
    suspend fun uploadImage(folderName: String, imageData: InputStream): Result<String>
}