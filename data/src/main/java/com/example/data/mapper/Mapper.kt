package com.example.data.mapper

import com.example.data.model.BeerData
import com.example.domain.model.Beer

internal fun BeerData.toDomainModel(): Beer {
    return Beer(
        id, name, firstBrewed, description, imageUrl, foodPairing, brewTips
    )
}