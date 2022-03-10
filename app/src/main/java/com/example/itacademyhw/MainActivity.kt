package com.example.itacademyhw

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import com.example.data.model.NightMode
import com.example.itacademyhw.managers.SharedPrefersManager
import org.koin.android.ext.android.inject

class MainActivity: AppCompatActivity(R.layout.activity_main) {

    private val prefsManager: SharedPrefersManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        AppCompatDelegate.setDefaultNightMode(
            when(prefsManager.nightMode) {
                NightMode.DARK ->  AppCompatDelegate.MODE_NIGHT_YES
                NightMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}