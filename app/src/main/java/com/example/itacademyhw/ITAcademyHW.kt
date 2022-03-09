package com.example.itacademyhw

import android.app.Application
import com.example.itacademyhw.DI.*
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
                repositoryModule,
                useCasesModule,
                sharedPrefsModule
            )
        }
    }
}