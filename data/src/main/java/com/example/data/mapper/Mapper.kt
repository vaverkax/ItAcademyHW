package com.example.data.mapper

import com.example.data.model.BeerData
import com.example.data.model.BeerDataDB
import com.example.domain.model.Beer

internal fun BeerData.toDomainModel(): Beer {
    return Beer(
        id, name, firstBrewed, description, imageUrl, brewTips
    )
}

internal fun Beer.toDatabaseModel(): BeerDataDB { // почему не могу вызвать при маппинге?
    return BeerDataDB(
        id, name, firstBrewed, description, imageUrl, brewTips
    )
}