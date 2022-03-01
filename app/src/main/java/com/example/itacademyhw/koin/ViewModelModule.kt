package com.example.itacademyhw.koin

import com.example.itacademyhw.BeerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { BeerViewModel(get()) }
}