package com.moviles.proyecto1.view

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.widget.doOnTextChanged
import com.moviles.proyecto1.viewmodel.LoginViewModel
import androidx.activity.viewModels
import com.moviles.proyecto1.model.UserRequest
import android.content.Context
import android.appwidget.AppWidgetManager
import android.content.Intent


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private var fromWidget = false


    companion object {
        private const val PREFS_NAME = "user_session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        fromWidget = intent.getBooleanExtra("from_widget", false)
        setupObservers()
        setupListeners()
    }

    private fun saveUserSession() {
        val sharedPref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    private fun setupObservers(){
        loginViewModel.isRegister.observe(this){ userResponse ->
            if (userResponse.isRegister){
                saveUserSession()
                Toast.makeText(this, userResponse.message, Toast.LENGTH_SHORT).show()

                if(fromWidget){
                    updateWidget()
                    finishAffinity()
                }else{
                    goToHomeInventory()
                }
            }
            else{
                Toast.makeText(this, userResponse.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(){
        val email = binding.IEmail.text.toString().trim()
        val pass = binding.IPass.text.toString().trim()

        if(email.isNotEmpty() && pass.isNotEmpty()){
            val userRequest = UserRequest(email, pass)
            loginViewModel.registerUser(userRequest)
        }
    }

    private fun loginUser(){
        val email = binding.IEmail.text.toString().trim()
        val pass = binding.IPass.text.toString().trim()

        loginViewModel.loginUser(email, pass) { isLogin ->
            if (isLogin) {
                saveUserSession()
                Toast.makeText(this, "Login Exitoso", Toast.LENGTH_SHORT).show()
                if(fromWidget){
                    updateWidget()
                    finishAffinity()
                }else{
                    goToHomeInventory()
                }
            } else {
                Toast.makeText(this, "Login incorrecto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateWidget(){
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val ids = appWidgetManager.getAppWidgetIds(
            android.content.ComponentName(this, Widget::class.java)
        )

        val intent = Intent(this, Widget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        }
            sendBroadcast(intent)
    }

    private fun goToHomeInventory(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupListeners() {

        binding.RegisterTV.setOnClickListener {
            registerUser()
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
        binding.IEmail.doOnTextChanged { _, _, _, _ ->
            updateButtons()
        }

        binding.IPass.doOnTextChanged { _, _, _, _ ->
            updateButtons()
            changeBtnState()
        }
    }

    private fun updateButtons() {
        val email = binding.IEmail.text?.toString()?.trim().orEmpty()
        val pass  = binding.IPass.text?.toString()?.trim().orEmpty()

        val areFilled = email.isNotEmpty() && pass.isNotEmpty()

        // Botón Login
        binding.btnLogin.isEnabled = areFilled
        binding.btnLogin.setTextColor(
            ContextCompat.getColor(
                this,
                if (areFilled) {
                    R.color.white
                }

                else R.color.specific_gray
            )
        )
        binding.btnLogin.setTypeface(
            binding.btnLogin.typeface,
            if (areFilled) Typeface.BOLD else Typeface.NORMAL
        )

        // Texto Register
        binding.RegisterTV.isEnabled = areFilled
        binding.RegisterTV.setTextColor(
            ContextCompat.getColor(
                this,
                if (areFilled) R.color.white else R.color.specific_gray
            )
        )
    }

    private fun changeBtnState(){
        val pass = binding.IPass.text?.toString()?.trim().orEmpty()

        if (pass.length < 6){
            binding.TFPass.error = "Mínimo 6 dígitos"

        }
        else{
            binding.TFPass.error = null
        }
    }

}