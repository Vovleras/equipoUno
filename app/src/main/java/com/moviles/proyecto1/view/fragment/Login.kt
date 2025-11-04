package com.moviles.proyecto1.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentLoginBinding

class Login : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        navigationLoginToHomeInventory()
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun navigationLoginToHomeInventory(){
        binding.btnHome.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_homeInventory)
        }
    }
}