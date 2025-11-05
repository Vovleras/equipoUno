package com.moviles.proyecto1.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
  }

}