package com.example.itacademyhw.di

import com.example.itacademyhw.managers.SharedPrefersManager
import org.koin.dsl.module

val sharedPrefsModule = module {
    single { SharedPrefersManager(get()) }
}