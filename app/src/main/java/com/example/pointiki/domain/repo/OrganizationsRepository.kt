package com.example.pointiki.domain.repo

import com.example.pointiki.models.Organization
import com.example.pointiki.utils.Result

interface OrganizationsRepository {
    suspend fun getOrganizations(): Result<List<Organization>>
}