package com.example.pointiki.data.source

import com.example.pointiki.models.Organization
import com.example.pointiki.utils.Result

interface OrganizationsDataSource {
    suspend fun getOrganizations(): Result<List<Organization>>
}