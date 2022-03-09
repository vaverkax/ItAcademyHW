package com.example.data.model

import com.google.gson.annotations.SerializedName

data class BeerData(
    val id: Long,
    val name: String,
    @SerializedName("first_brewed")
    val firstBrewed: String,
    val description: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("brewers_tips")
    val brewTips: String
)