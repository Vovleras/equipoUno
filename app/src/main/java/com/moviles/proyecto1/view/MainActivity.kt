package com.moviles.proyecto1.view
import dagger.hilt.android.AndroidEntryPoint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.ActivityMainBinding
import android.content.Context
import android.content.Intent


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val isLogged = sharedPref.getBoolean("is_logged_in", false)

    if(!isLogged){
      val intent = Intent(this, LoginActivity::class.java)
      startActivity(intent)
      finish()
      return
    }
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
      val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
      view.setPadding(view.paddingLeft, top, view.paddingRight, view.paddingBottom)
      insets
    }
  }

}