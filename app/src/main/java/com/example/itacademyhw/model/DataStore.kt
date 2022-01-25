package com.example.itacademyhw.model

import kotlinx.coroutines.delay
import java.util.*

class DataStore {
    suspend fun fetchData(): List<String> {
        delay(1000)
        return List(10) {
            UUID.randomUUID().toString()
        }
    }
}