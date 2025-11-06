package com.moviles.proyecto1.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentDetailProductBinding

class DetailProduct : Fragment() {
    private lateinit var binding: FragmentDetailProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailProductBinding.inflate(inflater)
        navigationDetailToEdit()
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun navigationDetailToEdit(){
        binding.btnEliminar.setOnClickListener {
            findNavController().navigate(R.id.action_detailProduct_to_editProduct)
        }
    }
}