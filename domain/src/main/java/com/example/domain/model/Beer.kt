package com.example.domain.model

data class Beer (
    val id: Long,
    val name: String,
    val firstBrewed: String,
    val description: String,
    val imageUrl: String,
    val foodPairing: List<String>,
    val brewTips: String)