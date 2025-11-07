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
    private lateinit var biometricPrompt: BiometricPrompt

    // --- Start of Fix ---
    // 1. Define the BiometricAuthListener interface
    interface BiometricAuthListener {
        fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult)
        fun onBiometricAuthenticateError(errorCode: Int, errorMessage: String)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.lifecycleOwner = this
        navigationLoginToHomeInventory()
        biometricEvent()
        return binding.root
    }

    private fun navigationLoginToHomeInventory() {
        binding.loginInventory.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_homeInventory)
        }
    }

    //setting up a biometric


    /*
     * Check whether the Device is Capable of the Biometric
     */
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

    // Initiate the Biometric Prompt
    private fun initBiometricPrompt(
        activity: AppCompatActivity,
        listener: BiometricAuthListener
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                listener.onBiometricAuthenticateError(errorCode, errString.toString())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                listener.onBiometricAuthenticateSuccess(result)
                findNavController().navigate(R.id.action_login_to_homeInventory)
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }

    // Display the Biometric Prompt
    fun showBiometricPrompt(
        activity: AppCompatActivity,
        listener: BiometricAuthListener,
        cryptoObject: BiometricPrompt.CryptoObject? = null,
    ) {
        val promptInfo = setBiometricPromptInfo()

        val biometricPrompt = initBiometricPrompt(activity, listener)
        biometricPrompt.apply {
            if (cryptoObject == null) authenticate(promptInfo)
            else authenticate(promptInfo, cryptoObject)
        }
    }
    //function to show biometric prompt
    private fun biometricEvent() {
        if (isBiometricReady(requireContext())) {
            binding.lottieFingerprint.setOnClickListener {
                showBiometricPrompt(
                    activity = requireActivity() as AppCompatActivity,
                    listener = object : BiometricAuthListener {
                        override fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult) {
                            // Already handled in initBiometricPrompt
                        }

                        override fun onBiometricAuthenticateError(errorCode: Int, errorMessage: String) {
                            Toast.makeText(requireContext(), "Authentication error: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    },
                    cryptoObject = null,
                )
            }
        } else {
            Toast.makeText(requireContext(), "No biometric feature perform on this device", Toast.LENGTH_SHORT).show()
        }
    }

}