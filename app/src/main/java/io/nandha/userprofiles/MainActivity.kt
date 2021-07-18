package io.nandha.userprofiles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.nandha.userprofiles.databinding.ActivityMainBinding

class MainActivity() : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)
    }
}