package com.bala.nytnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bala.nytnews.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MainActivityBinding.inflate(layoutInflater).root)
    }
}