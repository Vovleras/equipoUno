package com.moviles.proyecto1.view

import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
            updateButtons()
        }

        binding.IPass.doOnTextChanged { _, _, _, _ ->
            updateButtons()
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
}