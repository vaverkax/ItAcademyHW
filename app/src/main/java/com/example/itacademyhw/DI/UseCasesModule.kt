package com.example.itacademyhw.DI

import com.example.domain.useCases.GetBeersUseCase
import org.koin.dsl.module

val useCasesModule = module {
    factory { GetBeersUseCase(get()) }
}