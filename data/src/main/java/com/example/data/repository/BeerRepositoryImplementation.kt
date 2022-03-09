package com.example.data.repository

import com.example.data.api.BeerAPI
import com.example.data.mapper.toDomainModel
import com.example.domain.model.Beer
import com.example.domain.repository.BeerRepository

class BeerRepositoryImplementation(private val beerApi: BeerAPI): BeerRepository {
    override suspend fun getBeersData(page: Int, perPage: Int): Result<List<Beer>> = runCatching {
        beerApi.getBeers(page, perPage)
    }.map { beerList -> beerList.map { it.toDomainModel() } }
}