package com.moviles.proyecto1.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentHomeInventoryBinding

class HomeInventory : Fragment() {
    private lateinit var binding: FragmentHomeInventoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeInventoryBinding.inflate(inflater)
        navigationHomeInventoryToDetails()
        navigationHomeInventoryToAdd()
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun navigationHomeInventoryToDetails(){
        binding.btnDetails.setOnClickListener {
            findNavController().navigate(R.id.action_homeInventory_to_detailProduct)
        }
    }

    private fun navigationHomeInventoryToAdd(){
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeInventory_to_addProduct)
        }
    }

}