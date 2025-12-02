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


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val PREFS_NAME = "user_session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val COME_WIDGET = "from_widget"
        private const val KEY_IS_FROM_WIDGET = "is_from_widget"
    }

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
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

    private fun isFromWidget(): Boolean {
        val sharedPref = this.getSharedPreferences(COME_WIDGET, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(KEY_IS_FROM_WIDGET, false)
    }

    private fun registerUser(){
        val email = binding.IEmail.text.toString().trim()
        val pass = binding.IPass.text.toString().trim()
        val userRequest = UserRequest(email, pass)

        if(email.isNotEmpty() && pass.isNotEmpty()){
            loginViewModel.registerUser(userRequest)
            loginViewModel.isRegister.observe(this){ userResponse ->
                if (userResponse.isRegister){
                    saveUserSession()
                    Toast.makeText(this, userResponse.message, Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, userResponse.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loginUser(){
        val email = binding.IEmail.text.toString().trim()
        val pass = binding.IPass.text.toString().trim()

        loginViewModel.loginUser(email, pass) { isLogin ->
            if (isLogin) {
                saveUserSession()
                Toast.makeText(this, "Login Exitoso", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login incorrecto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
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