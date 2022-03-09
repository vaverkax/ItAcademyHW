package com.example.itacademyhw.DI

import com.example.data.db.BeerDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single{ BeerDatabase.create(androidContext()) }
    single { get<BeerDatabase>().beerDao() }
    single { get<BeerDatabase>().remoteKeysDao() }
}