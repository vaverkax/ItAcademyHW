package com.example.domain.repository

import com.example.domain.model.Beer

interface BeerRepository {
    suspend fun getBeersData(page: Int, perPage: Int): Result<List<Beer>>
}