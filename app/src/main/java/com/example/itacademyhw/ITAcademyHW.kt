package com.example.itacademyhw

import android.app.Application
import com.example.itacademyhw.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ITAcademyHW : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ITAcademyHW)
            modules(
                networkModule,
                viewModelModule,
                sharedPrefsModule,
                locationModule,
                beerRepositoryModule,
                databaseModule
            )
        }
    }
}