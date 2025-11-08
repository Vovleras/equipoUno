package com.moviles.proyecto1.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
      val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
      view.setPadding(view.paddingLeft, top, view.paddingRight, view.paddingBottom)
      insets
    }
  }

}