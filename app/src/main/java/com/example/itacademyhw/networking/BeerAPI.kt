package com.example.itacademyhw.networking

import com.example.itacademyhw.model.BeerData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerAPI {
    @GET("beers")
    fun getBeers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<List<BeerData>>

    @GET("beers")
    fun searchBeer(
        @Query("beer_name") beerName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<List<BeerData>>

}
