package com.example.nasa_mars_api_service.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nasa_mars_api_service.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideActionBar()
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}