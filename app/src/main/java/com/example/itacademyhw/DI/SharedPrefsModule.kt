package com.example.itacademyhw.DI

import com.example.itacademyhw.Managers.SharedPrefersManager
import org.koin.dsl.module

val sharedPrefsModule = module {
    single { SharedPrefersManager(get()) }
}