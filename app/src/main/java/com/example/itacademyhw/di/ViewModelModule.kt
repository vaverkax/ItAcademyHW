package com.example.itacademyhw.di

import com.example.itacademyhw.viewModel.BeerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { BeerViewModel( get()) }
}