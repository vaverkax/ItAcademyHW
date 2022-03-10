package com.example.itacademyhw.di

import com.example.data.repository.BeerRepositoryImplementation
import org.koin.dsl.module

val beerRepositoryModule = module {
    single { BeerRepositoryImplementation(get(), get()) }
}