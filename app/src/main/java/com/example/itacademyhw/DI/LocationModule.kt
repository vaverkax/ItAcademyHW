package com.example.itacademyhw.DI

import com.example.itacademyhw.LocationService
import org.koin.dsl.module

val locationModule = module {
    single{ LocationService (get()) }
}