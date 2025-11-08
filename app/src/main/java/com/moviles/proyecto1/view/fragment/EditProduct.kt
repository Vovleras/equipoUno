package com.moviles.proyecto1.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentEditProductBinding

class EditProduct : Fragment() {
    private lateinit var binding: FragmentEditProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProductBinding.inflate(inflater)
        setupToolbar()
        binding.btnEditar.isEnabled = false
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun setupToolbar(){
        binding.contentToolbar.title = "Editar Producto"
        binding.contentToolbar.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_editProduct_to_detailProduct)
        }

    }
}