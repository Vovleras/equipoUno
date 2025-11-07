package com.moviles.proyecto1.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentLoginBinding
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.widget.Toast

class Login : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    companion object {
        private const val PREFS_NAME = "user_session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.lifecycleOwner = this
        checkUserSession()
        return binding.root
    }

    // Funciones para guardar y verificar la sesión del usuario

    private fun saveUserSession() {
        val sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Verificar si el dispositivo tiene capacidad biométrica
    private fun hasBiometricCapability(context: Context): Int {
        return BiometricManager.from(context).canAuthenticate(BIOMETRIC_STRONG)
    }

    fun isBiometricReady(context: Context) =
        hasBiometricCapability(context) == BiometricManager.BIOMETRIC_SUCCESS
    private fun setBiometricPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.tittle_biometrics))
            .setSubtitle(getString(R.string.subtitle_biometrics))
            .setDescription(getString(R.string.description_biometrics))
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .setNegativeButtonText(getString(R.string.cancelar))
            .build()

    }

    // Iniciar BiometricPrompt
    private fun initBiometricPrompt(
        activity: AppCompatActivity
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(requireContext(), "Error: $errString", Toast.LENGTH_SHORT).show()
                binding.lottieFingerprint.isEnabled = true
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                binding.lottieFingerprint.isEnabled = true
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                saveUserSession()
                binding.lottieFingerprint.isEnabled = true
                findNavController().navigate(R.id.action_login_to_homeInventory)
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }

    // Mostrar la ventana de autenticación biométrica
    fun showBiometricPrompt(
        activity: AppCompatActivity
    ) {
        val promptInfo = setBiometricPromptInfo()
        val biometricPrompt = initBiometricPrompt(activity)
        biometricPrompt.apply {
            authenticate(promptInfo)
        }
    }

    // Evento para la imágen dinámica de autenticación biométrica
    private fun biometricEvent() {
        if (isBiometricReady(requireContext())) {
            binding.lottieFingerprint.setOnClickListener {
                binding.lottieFingerprint.isEnabled = false
                showBiometricPrompt(requireActivity() as AppCompatActivity)
            }
        } else {
            Toast.makeText(requireContext(), "No biometric feature perform on this device", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkUserSession() {
        if (isUserLoggedIn()) {
            findNavController().navigate(R.id.action_login_to_homeInventory)
        }
        else{
            biometricEvent()
        }
    }

}