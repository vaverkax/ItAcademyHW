package com.example.itacademyhw.mapper

import com.example.data.model.BeerDataDB
import com.example.domain.model.Beer

internal fun Beer.toDatabaseModel(): BeerDataDB {
    return BeerDataDB(
        id, name, firstBrewed, description, imageUrl, brewTips
    )
}