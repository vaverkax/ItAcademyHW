package com.example.itacademyhw.networking

import com.example.itacademyhw.model.BeerData
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerAPI {
    @GET("beers")
    suspend fun getBeers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<BeerData>
}
