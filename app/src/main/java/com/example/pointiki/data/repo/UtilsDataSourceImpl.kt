package com.example.pointiki.data.repo

import com.example.pointiki.data.source.UtilsDataSource
import com.example.pointiki.domain.repo.UtilsRepository
import com.example.pointiki.utils.Result
import java.io.InputStream
import javax.inject.Inject

class UtilsDataSourceImpl @Inject constructor(
    private val dataSource: UtilsDataSource
) : UtilsRepository {
    override suspend fun uploadImage(folderName: String, imageData: InputStream): Result<String> {
        return  dataSource.uploadImage(folderName, imageData)
    }
}