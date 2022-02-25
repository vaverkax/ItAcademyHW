package com.example.itacademyhw.DI

import com.example.itacademyhw.ViewModel.BeerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { BeerViewModel(get()) }
}