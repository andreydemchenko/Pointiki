package com.example.pointiki.data.source

import com.example.pointiki.utils.Result
import java.io.InputStream

interface UtilsDataSource {
    suspend fun uploadImage(folderName: String, imageData: InputStream): Result<String>
}