package com.example.itacademyhw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.itacademyhw.fragment.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.containerFragment, MainFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}