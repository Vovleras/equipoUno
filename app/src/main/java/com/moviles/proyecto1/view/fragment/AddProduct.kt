package com.moviles.proyecto1.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentAddProductBinding

class AddProduct : Fragment() {
    private lateinit var binding: FragmentAddProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }
}