package com.moviles.proyecto1.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentEditProductBinding

class EditProduct : Fragment() {
    private lateinit var binding: FragmentEditProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProductBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }
}