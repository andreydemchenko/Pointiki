package com.example.pointiki.data.source

import com.example.pointiki.models.Organization
import com.example.pointiki.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class FirebaseOrganizationsDataSource @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : OrganizationsDataSource {

    override suspend fun getOrganizations(): Result<List<Organization>> {
        return try {
            val organizations = firebaseFirestore
                .collection(COLLECTION_ORGANIZATIONS)
                .get()
                .await()
                .documents
                .mapNotNull { snapshot ->
                    val id = snapshot.getString(FIELD_ID)
                    val name = snapshot.getString(FIELD_NAME)
                    val imageUrl = snapshot.getString(FIELD_IMAGE_URL)
                    if (name != null && imageUrl != null) {
                        Organization(
                            id = UUID.fromString(id),
                            name = name,
                            imageUrl = imageUrl
                        )
                    } else {
                        null
                    }
                }
            Result.Success(organizations)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    companion object {
        const val COLLECTION_ORGANIZATIONS = "organizations"
        const val FIELD_ID = "id"
        const val FIELD_IMAGE_URL = "imageUrl"
        const val FIELD_NAME = "name"
    }
}