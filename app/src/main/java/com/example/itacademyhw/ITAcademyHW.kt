package com.example.itacademyhw

import android.app.Application
import com.example.itacademyhw.koin.networkModule
import com.example.itacademyhw.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ITAcademyHW : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ITAcademyHW)
            modules(
                networkModule,
                viewModelModule
            )
        }
    }
}