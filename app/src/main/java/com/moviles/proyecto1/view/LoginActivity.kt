package com.moviles.proyecto1.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.widget.doOnTextChanged



@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setupListeners()

    }

    private fun setupListeners() {
        binding.IEmail.doOnTextChanged { _, _, _, _ ->
            updateLoginButton()
        }

        binding.IPass.doOnTextChanged { _, _, _, _ ->
            updateLoginButton()
        }
    }

    private fun updateLoginButton() {
        val email = binding.IEmail.text?.toString()?.trim().orEmpty()
        val pass = binding.IPass.text?.toString()?.trim().orEmpty()

        binding.btnLogin.isEnabled = email.isNotEmpty() && pass.isNotEmpty()
        binding.btnLogin.isVisible = email.isNotEmpty() && pass.isNotEmpty()
    }
}