package com.example.domain.useCases

import com.example.domain.model.Beer
import com.example.domain.repository.BeerRepository

class GetBeersUseCase(private val beerRepository: BeerRepository) {
    suspend operator fun invoke(page: Int, perPage: Int): Result<List<Beer>> {
        return beerRepository.getBeersData(page,perPage)
    }
}