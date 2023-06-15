package com.example.pointiki.data.repo

import com.example.pointiki.data.source.OrganizationsDataSource
import com.example.pointiki.domain.repo.OrganizationsRepository
import com.example.pointiki.models.Organization
import com.example.pointiki.utils.Result
import javax.inject.Inject

class OrganizationsRepositoryImpl @Inject constructor(
    private val dataSource: OrganizationsDataSource
    ) : OrganizationsRepository {
    override suspend fun getOrganizations(): Result<List<Organization>> = dataSource.getOrganizations()
}