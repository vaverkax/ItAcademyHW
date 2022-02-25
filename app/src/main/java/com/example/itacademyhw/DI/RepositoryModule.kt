package com.example.itacademyhw.DI

import com.example.data.repository.BeerRepositoryImplementation
import com.example.domain.repository.BeerRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<BeerRepository> { BeerRepositoryImplementation(get()) }
}