package com.example.itacademyhw.provider

import com.example.itacademyhw.model.DataStore

object ServiceProvider {
    private val dataStore by lazy { DataStore() }

    fun provideDataStore(): DataStore {
        return dataStore
    }
}