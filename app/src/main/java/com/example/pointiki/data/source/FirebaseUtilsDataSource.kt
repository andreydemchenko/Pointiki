package com.example.pointiki.data.source

import com.example.pointiki.utils.Result
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

class FirebaseUtilsDataSource @Inject constructor(
    private val firebaseStorage: FirebaseStorage
) : UtilsDataSource {

    override suspend fun uploadImage(folderName: String, imageData: InputStream): Result<String> {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileName = "${UUID.randomUUID()}"
        val imageRef = storageRef.child("$folderName/$fileName")

        return try {
            val uploadTask = imageRef.putStream(imageData)
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: StorageException.fromException(Exception("Unknown error occurred while uploading image"))
                }
                imageRef.downloadUrl
            }.await()

            Result.Success(urlTask.toString())
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}