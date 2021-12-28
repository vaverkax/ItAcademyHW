package com.example.itacademyhw.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object BeerService {
    private val retrofit  by lazy(LazyThreadSafetyMode.NONE) { initService() }
    val beerApi by lazy(LazyThreadSafetyMode.NONE) { retrofit.create<BeerAPI>() }

    private fun initService(): Retrofit {
        val client = OkHttpClient.Builder().build()
        return Retrofit.Builder()
            .baseUrl("https://api.punkapi.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}